package com.hk.mgmt.dto.module;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "모듈 수정 요청")
public record ModuleUpdateRequest(

        @Schema(description = "모듈명", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "비고")
        @Size(max = 255)
        String remark,

        @Schema(description = "상태", allowableValues = {"E", "D"}, defaultValue = "E")
        @Pattern(regexp = "^[ED]$")
        String status,

        @Schema(description = "순서")
        Integer seq,

        @Schema(description = "아이콘 클래스", example = "ti ti-home")
        @Size(max = 100)
        String icon,

        @Schema(description = "URL", example = "/module")
        @Size(max = 255)
        String url,

        @Schema(description = "모듈 메뉴 목록 (전체 교체)")
        @Valid
        List<ModuleMenuItemRequest> menus

) {}