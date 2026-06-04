package com.hk.mgmt.dto.role;

import com.hk.mgmt.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "역할 목록 항목")
public record RoleListDto(
        String id,
        String name,
        String remark,
        String regDate
) {
    public static RoleListDto from(Role role) {
        return new RoleListDto(role.getId(), role.getName(), role.getRemark(), role.getRegDate());
    }
}