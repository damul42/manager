package com.hk.mgmt.dto.module;

import com.hk.mgmt.domain.Module;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "권한 설정용 모듈·메뉴 트리")
public record ModuleWithMenusDto(
        String id,
        String name,
        String icon,
        Integer seq,
        List<MenuSimpleDto> menus
) {
    public static ModuleWithMenusDto from(Module module) {
        List<MenuSimpleDto> menus = module.getMenus().stream()
                .filter(m -> "E".equals(m.getStatus()))
                .map(MenuSimpleDto::from)
                .toList();
        return new ModuleWithMenusDto(module.getId(), module.getName(), module.getIcon(), module.getSeq(), menus);
    }
}