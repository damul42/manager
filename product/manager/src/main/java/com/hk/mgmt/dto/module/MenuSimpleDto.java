package com.hk.mgmt.dto.module;

import com.hk.mgmt.domain.ModuleMenu;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메뉴 간단 정보")
public record MenuSimpleDto(
        String id,
        String name,
        String icon,
        Integer seq
) {
    public static MenuSimpleDto from(ModuleMenu menu) {
        return new MenuSimpleDto(menu.getId(), menu.getName(), menu.getIcon(), menu.getSeq());
    }
}