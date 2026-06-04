package com.hk.mgmt.dto.role;

import com.hk.mgmt.domain.Role;
import com.hk.mgmt.domain.RoleSetting;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "역할 상세")
public record RoleDetailDto(
        String id,
        String name,
        String remark,
        String regId,
        String regDate,
        String uptId,
        String uptDate,
        List<RoleSettingItemDto> settings
) {
    public static RoleDetailDto from(Role role, List<RoleSetting> settings) {
        return new RoleDetailDto(
                role.getId(), role.getName(), role.getRemark(),
                role.getRegId(), role.getRegDate(),
                role.getUptId(), role.getUptDate(),
                settings.stream().map(RoleSettingItemDto::from).toList()
        );
    }
}