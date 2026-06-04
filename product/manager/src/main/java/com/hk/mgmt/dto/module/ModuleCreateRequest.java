package com.hk.mgmt.dto.module;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "모듈 등록 요청")
public record ModuleCreateRequest(

        @Schema(description = "모듈명", example = "사용자 관리", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "비고")
        @Size(max = 255)
        String remark,

        @Schema(description = "상태", allowableValues = {"E", "D"}, defaultValue = "E", example = "E")
        @Pattern(regexp = "^[ED]$")
        String status,

        @Schema(description = "순서", example = "1")
        Integer seq,

        @Schema(description = "아이콘 클래스", example = "ti ti-home")
        @Size(max = 100)
        String icon,

        @Schema(description = "URL", example = "/module")
        @Size(max = 255)
        String url,

        @Schema(description = "모듈 메뉴 목록")
        @Valid
        List<ModuleMenuItemRequest> menus

) {}