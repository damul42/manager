package com.hk.mgmt.dto.module;

import com.hk.mgmt.domain.Module;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모듈 목록 항목")
public record ModuleListDto(

        @Schema(description = "모듈 ID (UUID)")
        String id,

        @Schema(description = "모듈명")
        String name,

        @Schema(description = "비고")
        String remark,

        @Schema(description = "상태", allowableValues = {"E", "D"}, example = "E")
        String status,

        @Schema(description = "순서")
        Integer seq,

        @Schema(description = "아이콘 클래스", example = "ti ti-home")
        String icon,

        @Schema(description = "URL", example = "/module")
        String url

) {
    public static ModuleListDto from(Module module) {
        return new ModuleListDto(
                module.getId(),
                module.getName(),
                module.getRemark(),
                module.getStatus(),
                module.getSeq(),
                module.getIcon(),
                module.getUrl()
        );
    }
}