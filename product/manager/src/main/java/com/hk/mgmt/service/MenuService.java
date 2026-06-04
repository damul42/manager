package com.hk.mgmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.mgmt.config.JwtProperties;
import com.hk.mgmt.domain.Module;
import com.hk.mgmt.domain.ModuleMenu;
import com.hk.mgmt.domain.RoleSetting;
import com.hk.mgmt.domain.UserRole;
import com.hk.mgmt.dto.menu.MenuItemDto;
import com.hk.mgmt.dto.menu.MenuPermission;
import com.hk.mgmt.dto.menu.ModuleMenuDto;
import com.hk.mgmt.repository.ModuleMenuRepository;
import com.hk.mgmt.repository.ModuleRepository;
import com.hk.mgmt.repository.RoleSettingRepository;
import com.hk.mgmt.repository.UserRoleRepository;
import com.hk.mgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private static final String KEY_PREFIX    = "menu:";
    private static final String ACCESS_PREFIX = "access:";
    private static final String ENABLED = "E";
    // menuId가 null인 설정(모듈 전체 권한)을 구분하는 내부 키
    private static final String MODULE_LEVEL = "__MODULE__";

    private final UserRoleRepository sysUserRoleRepository;
    private final RoleSettingRepository sysRoleSettingRepository;
    private final ModuleRepository sysModuleRepository;
    private final ModuleMenuRepository sysModuleMenuRepository;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    /**
     * 로그인 시 호출 — 권한을 계산하여 Redis에 캐싱
     */
    public void buildAndCache(String userId, String email) {
        List<ModuleMenuDto> menu = buildMenuTree(userId);
        cache(email, menu);
    }

    /**
     * 요청마다 호출 — Redis에서 메뉴 조회 (캐시 미스 시 DB 재조회 후 캐싱)
     */
    public List<ModuleMenuDto> getMenu(String email) {
        String json = redisTemplate.opsForValue().get(KEY_PREFIX + email);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize menu cache for {}", email, e);
            }
        }

        // 캐시 미스 (Redis 재시작 등) — DB에서 재조회 후 캐싱
        return userRepository.findByEmail(email)
                .map(user -> {
                    buildAndCache(user.getId(), email);
                    return getMenuFromCache(email);
                })
                .orElse(List.of());
    }

    /**
     * 권한 변경 시 호출 — 해당 사용자의 캐시 무효화 후 즉시 재빌드
     */
    public void invalidateAndRebuild(String userId, String email) {
        redisTemplate.delete(KEY_PREFIX + email);
        redisTemplate.delete(ACCESS_PREFIX + email);
        buildAndCache(userId, email);
    }

    /**
     * 로그아웃 시 호출 — 캐시만 삭제 (재빌드 없음)
     */
    public void invalidateCache(String email) {
        redisTemplate.delete(KEY_PREFIX + email);
        redisTemplate.delete(ACCESS_PREFIX + email);
    }

    /**
     * 사용자에게 접근 가능한 URL이 하나라도 있는지 확인.
     * false이면 역할/메뉴가 아직 설정되지 않은 것으로 간주한다.
     */
    public boolean hasAccessibleUrls(String email) {
        return !getAccessibleUrls(email).isEmpty();
    }

    /**
     * 요청 URL이 사용자의 접근 가능 경로 내에 있는지 확인.
     * 캐시 미스 시 DB 재조회를 통해 복구한다.
     * 접근 가능 URL 또는 그 하위 경로이면 true.
     */
    public boolean isAccessible(String email, String requestUrl) {
        List<String> urls = getAccessibleUrls(email);
        if (urls.isEmpty()) return false;
        return urls.stream().anyMatch(url ->
                requestUrl.equals(url) || requestUrl.startsWith(url + "/"));
    }

    private List<String> getAccessibleUrls(String email) {
        String json = redisTemplate.opsForValue().get(ACCESS_PREFIX + email);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize access cache for {}", email, e);
            }
        }
        // 캐시 미스 — 메뉴 재빌드 후 재시도
        getMenu(email);
        String rebuilt = redisTemplate.opsForValue().get(ACCESS_PREFIX + email);
        if (rebuilt == null) return List.of();
        try {
            return objectMapper.readValue(rebuilt, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    private List<ModuleMenuDto> getMenuFromCache(String email) {
        String json = redisTemplate.opsForValue().get(KEY_PREFIX + email);
        if (json == null) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize menu cache for {}", email, e);
            return List.of();
        }
    }

    // -------------------------------------------------------------------------

    private List<ModuleMenuDto> buildMenuTree(String userId) {
        // 1. 사용자에게 할당된 Role ID 목록
        List<String> roleIds = sysUserRoleRepository.findAllByUserId(userId)
                .stream().map(UserRole::getRoleId).toList();

        if (roleIds.isEmpty()) return List.of();

        // 2. 해당 Role들의 모든 RoleSetting 조회
        List<RoleSetting> settings = sysRoleSettingRepository.findAllByRoleIdIn(roleIds);

        if (settings.isEmpty()) return List.of();

        // 3. (moduleId, menuId/MODULE_LEVEL) 기준으로 권한 Union (Y 우선)
        //    moduleId → { menuKey → MergedPermission }
        Map<String, Map<String, MergedPermission>> permMap = new LinkedHashMap<>();

        for (RoleSetting s : settings) {
            String menuKey = s.getMenuId() != null ? s.getMenuId() : MODULE_LEVEL;
            permMap.computeIfAbsent(s.getModuleId(), k -> new LinkedHashMap<>())
                   .computeIfAbsent(menuKey, k -> new MergedPermission())
                   .merge(s);
        }

        // 4. 활성화된 모듈 중 권한이 있는 것만 seq 순서로 로드
        Set<String> accessibleModuleIds = permMap.keySet();
        List<Module> modules = sysModuleRepository.findAllByStatusOrderBySeqAsc(ENABLED)
                .stream()
                .filter(m -> accessibleModuleIds.contains(m.getId()))
                .toList();

        // 5. 모듈별 메뉴 트리 구성
        List<ModuleMenuDto> result = new ArrayList<>();

        for (Module module : modules) {
            Map<String, MergedPermission> modulePermMap = permMap.get(module.getId());
            MergedPermission modulePerm = modulePermMap.getOrDefault(MODULE_LEVEL, new MergedPermission());

            // 활성화된 메뉴 중 접근 가능한 것 구성
            List<MenuItemDto> menuItems = new ArrayList<>();
            List<ModuleMenu> enabledMenus = sysModuleMenuRepository
                    .findAllByModule_IdAndStatusOrderBySeqAsc(module.getId(), ENABLED);

            for (ModuleMenu menu : enabledMenus) {
                MergedPermission menuPerm = modulePermMap.get(menu.getId());

                if (menuPerm == null) {
                    // 메뉴별 개별 설정이 없으면 모듈 레벨 권한 상속
                    if (!modulePerm.list) continue;
                    menuPerm = modulePerm;
                } else if (!menuPerm.list) {
                    continue;
                }

                menuItems.add(new MenuItemDto(
                        menu.getId(), menu.getName(), menu.getIcon(), menu.getUrl(),
                        menuPerm.toDto()
                ));
            }

            // 모듈 레벨 list 권한이 있거나, 접근 가능한 하위 메뉴가 하나라도 있으면 포함
            if (!modulePerm.list && menuItems.isEmpty()) continue;

            result.add(new ModuleMenuDto(
                    module.getId(), module.getName(), module.getIcon(), module.getUrl(),
                    modulePerm.toDto(), menuItems
            ));
        }

        return result;
    }

    private void cache(String email, List<ModuleMenuDto> menu) {
        try {
            Duration ttl = Duration.ofMillis(jwtProperties.accessTokenExpiration());

            // 사이드바 렌더링용 메뉴 트리
            redisTemplate.opsForValue().set(KEY_PREFIX + email,
                    objectMapper.writeValueAsString(menu), ttl);

            // URL 접근 제어용 평면 URL 목록
            redisTemplate.opsForValue().set(ACCESS_PREFIX + email,
                    objectMapper.writeValueAsString(extractAccessibleUrls(menu)), ttl);
        } catch (JsonProcessingException e) {
            log.error("Failed to cache menu for {}", email, e);
        }
    }

    private List<String> extractAccessibleUrls(List<ModuleMenuDto> modules) {
        List<String> urls = new ArrayList<>();
        for (ModuleMenuDto module : modules) {
            if (module.url() != null && !module.url().isBlank()) {
                urls.add(module.url());
            }
            for (MenuItemDto menu : module.menus()) {
                if (menu.url() != null && !menu.url().isBlank()) {
                    urls.add(menu.url());
                }
            }
        }
        return urls;
    }

    // 여러 Role의 권한을 OR로 합산하는 내부 집계 클래스
    private static class MergedPermission {
        boolean admin, list, read, write;

        void merge(RoleSetting s) {
            this.admin = this.admin || s.hasAdminPermission();
            this.list  = this.list  || s.hasListPermission();
            this.read  = this.read  || s.hasReadPermission();
            this.write = this.write || s.hasWritePermission();
        }

        MenuPermission toDto() {
            return new MenuPermission(admin, list, read, write);
        }
    }
}