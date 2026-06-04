package com.hk.mgmt.dto.module;

import com.hk.mgmt.domain.Module;
import com.hk.mgmt.domain.ModuleMenu;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "모듈 상세 정보")
public record ModuleDetailDto(

        @Schema(description = "모듈 ID (UUID)")
        String id,

        @Schema(description = "모듈명")
        String name,

        @Schema(description = "비고")
        String remark,

        @Schema(description = "상태", allowableValues = {"E", "D"})
        String status,

        @Schema(description = "순서")
        Integer seq,

        @Schema(description = "아이콘 클래스")
        String icon,

        @Schema(description = "URL")
        String url,

        @Schema(description = "등록자 ID")
        String regId,

        @Schema(description = "등록일시 (yyyyMMddHHmmss)")
        String regDate,

        @Schema(description = "수정자 ID")
        String uptId,

        @Schema(description = "수정일시 (yyyyMMddHHmmss)")
        String uptDate,

        @Schema(description = "모듈 메뉴 목록")
        List<ModuleMenuItemDto> menus

) {
    public static ModuleDetailDto from(Module module) {
        return from(module, module.getMenus());
    }

    public static ModuleDetailDto from(Module module, List<ModuleMenu> menus) {
        return new ModuleDetailDto(
                module.getId(), module.getName(), module.getRemark(), module.getStatus(),
                module.getSeq(), module.getIcon(), module.getUrl(),
                module.getRegId(), module.getRegDate(), module.getUptId(), module.getUptDate(),
                menus.stream().map(ModuleMenuItemDto::from).toList()
        );
    }
}