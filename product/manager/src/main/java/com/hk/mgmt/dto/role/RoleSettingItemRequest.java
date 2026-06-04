package com.hk.mgmt.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "역할 권한 설정 항목 요청")
public record RoleSettingItemRequest(
        @NotBlank String moduleId,
        String menuId,
        @Pattern(regexp = "^[YN]$") String admin,
        @Pattern(regexp = "^[YN]$") String list,
        @Pattern(regexp = "^[YN]$") String read,
        @Pattern(regexp = "^[YN]$") String write
) {}