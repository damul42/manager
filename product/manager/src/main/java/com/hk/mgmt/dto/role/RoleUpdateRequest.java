package com.hk.mgmt.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "역할 수정 요청")
public record RoleUpdateRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 255) String remark,
        @Valid List<RoleSettingItemRequest> settings
) {}