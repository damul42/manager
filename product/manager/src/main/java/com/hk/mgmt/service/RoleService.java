package com.hk.mgmt.service;

import com.hk.mgmt.domain.Role;
import com.hk.mgmt.domain.RoleSetting;
import com.hk.mgmt.domain.UserRole;
import com.hk.mgmt.dto.role.RoleCreateRequest;
import com.hk.mgmt.dto.role.RoleDetailDto;
import com.hk.mgmt.dto.role.RoleListDto;
import com.hk.mgmt.dto.role.RoleSettingItemRequest;
import com.hk.mgmt.dto.role.RoleUpdateRequest;
import com.hk.mgmt.repository.ModuleMenuRepository;
import com.hk.mgmt.repository.RoleRepository;
import com.hk.mgmt.repository.RoleSettingRepository;
import com.hk.mgmt.repository.UserRepository;
import com.hk.mgmt.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Set;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository        roleRepository;
    private final RoleSettingRepository roleSettingRepository;
    private final UserRoleRepository    userRoleRepository;
    private final UserRepository        userRepository;
    private final ModuleMenuRepository  moduleMenuRepository;
    private final MenuService           menuService;

    // ── 목록 ──────────────────────────────────────────────────────────

    public List<RoleListDto> getList() {
        return roleRepository.findAllByOrderByNameAsc()
                .stream().map(RoleListDto::from).toList();
    }

    // ── 상세 ──────────────────────────────────────────────────────────

    public RoleDetailDto getDetail(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + id));
        List<RoleSetting> settings = roleSettingRepository.findAllByRoleId(id);
        return RoleDetailDto.from(role, settings);
    }

    // ── 등록 ──────────────────────────────────────────────────────────

    @Transactional
    public RoleDetailDto create(RoleCreateRequest req) {
        if (roleRepository.findByName(req.name()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 역할명입니다.");
        }

        Role role = Role.create(req.name(), req.remark());
        role.initReg(now());
        roleRepository.save(role);

        List<RoleSetting> savedSettings = saveSettings(role.getId(), req.settings());
        return RoleDetailDto.from(role, savedSettings);
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Transactional
    public RoleDetailDto update(String id, RoleUpdateRequest req) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + id));

        roleRepository.findByName(req.name())
                .filter(r -> !r.getId().equals(id))
                .ifPresent(r -> { throw new IllegalArgumentException("이미 사용 중인 역할명입니다."); });

        role.update(req.name(), req.remark());
        role.initUpt(now());

        invalidateCachesForRole(id);
        roleSettingRepository.deleteAllByRoleId(id);
        List<RoleSetting> savedSettings = saveSettings(id, req.settings());

        return RoleDetailDto.from(role, savedSettings);
    }

    // ── 삭제 (물리 삭제) ──────────────────────────────────────────────

    @Transactional
    public void delete(String id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found: " + id);
        }
        invalidateCachesForRole(id);
        roleSettingRepository.deleteAllByRoleId(id);
        userRoleRepository.deleteAllByRoleId(id);
        roleRepository.deleteById(id);
    }

    // ── 내부 유틸 ─────────────────────────────────────────────────────

    private void invalidateCachesForRole(String roleId) {
        userRoleRepository.findAllByRoleId(roleId).stream()
                .map(UserRole::getUserId)
                .distinct()
                .forEach(uid -> userRepository.findById(uid)
                        .ifPresent(u -> menuService.invalidateCache(u.getEmail())));
    }

    private List<RoleSetting> saveSettings(String roleId, List<RoleSettingItemRequest> items) {
        if (items == null || items.isEmpty()) return List.of();

        // 메뉴가 존재하는 모듈 ID 집합: 해당 모듈의 module-level 설정은 저장하지 않음
        Set<String> modulesWithMenus = new java.util.HashSet<>(
                moduleMenuRepository.findModuleIdsHavingMenus()
        );

        return items.stream()
                .filter(item -> StringUtils.hasText(item.moduleId()))
                .filter(item -> {
                    // module-level 항목(menuId 없음)이면서 메뉴가 있는 모듈이면 제외
                    boolean isModuleLevel = !StringUtils.hasText(item.menuId());
                    return !(isModuleLevel && modulesWithMenus.contains(item.moduleId()));
                })
                .map(item -> roleSettingRepository.save(RoleSetting.create(
                        roleId,
                        item.moduleId(),
                        StringUtils.hasText(item.menuId()) ? item.menuId() : null,
                        defaultYN(item.admin()),
                        defaultYN(item.list()),
                        defaultYN(item.read()),
                        defaultYN(item.write())
                )))
                .toList();
    }

    private String defaultYN(String val) {
        return "Y".equals(val) ? "Y" : "N";
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}