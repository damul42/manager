package com.hk.mgmt.dto.role;

import com.hk.mgmt.domain.RoleSetting;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "역할 권한 설정 항목")
public record RoleSettingItemDto(
        String id,
        String moduleId,
        String menuId,
        String admin,
        String list,
        String read,
        String write,
        Integer pIndex
) {
    public static RoleSettingItemDto from(RoleSetting s) {
        return new RoleSettingItemDto(
                s.getId(), s.getModuleId(), s.getMenuId(),
                s.getAdmin(), s.getList(), s.getRead(), s.getWrite(), s.getPIndex()
        );
    }
}