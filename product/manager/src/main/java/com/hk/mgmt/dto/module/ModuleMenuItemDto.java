package com.hk.mgmt.dto.module;

import com.hk.mgmt.domain.ModuleMenu;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모듈 메뉴 항목")
public record ModuleMenuItemDto(

        @Schema(description = "메뉴 ID (UUID)")
        String id,

        @Schema(description = "순서")
        Integer seq,

        @Schema(description = "메뉴명")
        String name,

        @Schema(description = "아이콘 클래스")
        String icon,

        @Schema(description = "URL")
        String url,

        @Schema(description = "상태")
        String status

) {
    public static ModuleMenuItemDto from(ModuleMenu m) {
        return new ModuleMenuItemDto(
                m.getId(), m.getSeq(), m.getName(), m.getIcon(), m.getUrl(), m.getStatus()
        );
    }
}