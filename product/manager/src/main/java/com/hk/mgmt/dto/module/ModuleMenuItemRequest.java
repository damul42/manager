package com.hk.mgmt.dto.module;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "모듈 메뉴 항목 요청")
public record ModuleMenuItemRequest(

        @Schema(description = "메뉴 ID (기존 항목 수정 시, 신규 시 null)")
        String id,

        @Schema(description = "순서")
        Integer seq,

        @Schema(description = "메뉴명", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "아이콘 클래스", example = "ti ti-list")
        @Size(max = 100)
        String icon,

        @Schema(description = "URL", example = "/module/list")
        @Size(max = 255)
        String url,

        @Schema(description = "상태", allowableValues = {"E", "D"}, defaultValue = "E")
        @Pattern(regexp = "^[ED]$")
        String status

) {}