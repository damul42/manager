package com.hk.mgmt.service;

import com.hk.mgmt.domain.Module;
import com.hk.mgmt.domain.ModuleMenu;
import com.hk.mgmt.dto.module.ModuleCreateRequest;
import com.hk.mgmt.dto.module.ModuleDetailDto;
import com.hk.mgmt.dto.module.ModuleListDto;
import com.hk.mgmt.dto.module.ModuleMenuItemRequest;
import com.hk.mgmt.dto.module.ModuleUpdateRequest;
import com.hk.mgmt.dto.module.ModuleWithMenusDto;
import com.hk.mgmt.repository.ModuleMenuRepository;
import com.hk.mgmt.repository.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModuleService {

    private final ModuleRepository     moduleRepository;
    private final ModuleMenuRepository moduleMenuRepository;

    // ── 권한 설정용 모듈·메뉴 트리 ────────────────────────────────────

    public List<ModuleWithMenusDto> getModulesWithMenus() {
        return moduleRepository.findAllByStatusOrderBySeqAsc("E")
                .stream().map(ModuleWithMenusDto::from).toList();
    }

    // ── 목록 ──────────────────────────────────────────────────────────

    public List<ModuleListDto> getList(String name, String status) {
        String nameParam   = StringUtils.hasText(name)   ? name   : null;
        String statusParam = StringUtils.hasText(status) ? status : null;
        return moduleRepository.search(nameParam, statusParam)
                .stream().map(ModuleListDto::from).toList();
    }

    // ── 상세 ──────────────────────────────────────────────────────────

    public ModuleDetailDto getDetail(String id) {
        return moduleRepository.findById(id)
                .map(ModuleDetailDto::from)
                .orElseThrow(() -> new EntityNotFoundException("Module not found: " + id));
    }

    // ── 등록 ──────────────────────────────────────────────────────────

    @Transactional
    public ModuleDetailDto create(ModuleCreateRequest req) {
        if (moduleRepository.findByName(req.name()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 모듈명입니다.");
        }

        String status  = StringUtils.hasText(req.status()) ? req.status() : "E";
        String regDate = now();

        Module module = Module.create(req.name(), req.remark(), status, req.seq(), req.icon(), req.url());
        module.initReg(regDate);
        moduleRepository.save(module);

        List<ModuleMenu> savedMenus = saveMenus(module, req.menus());

        return ModuleDetailDto.from(module, savedMenus);
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Transactional
    public ModuleDetailDto update(String id, ModuleUpdateRequest req) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Module not found: " + id));

        moduleRepository.findByName(req.name())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> { throw new IllegalArgumentException("이미 사용 중인 모듈명입니다."); });

        String status = StringUtils.hasText(req.status()) ? req.status() : "E";
        module.update(req.name(), req.remark(), status, req.seq(), req.icon(), req.url());
        module.initUpt(now());

        moduleMenuRepository.deleteAllByModuleId(id);
        saveMenus(module, req.menus());

        return ModuleDetailDto.from(module);
    }

    // ── 삭제 (소프트 삭제) ─────────────────────────────────────────────

    @Transactional
    public void delete(String id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Module not found: " + id));
        module.changeStatus("D");
    }

    // ── 내부 유틸 ─────────────────────────────────────────────────────

    private List<ModuleMenu> saveMenus(Module module, List<ModuleMenuItemRequest> items) {
        if (items == null || items.isEmpty()) return List.of();
        List<ModuleMenu> result = new java.util.ArrayList<>();
        for (ModuleMenuItemRequest m : items) {
            String menuStatus = StringUtils.hasText(m.status()) ? m.status() : "E";
            result.add(moduleMenuRepository.save(
                    ModuleMenu.create(module, m.seq(), m.name(), m.icon(), m.url(), menuStatus)
            ));
        }
        return result;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}