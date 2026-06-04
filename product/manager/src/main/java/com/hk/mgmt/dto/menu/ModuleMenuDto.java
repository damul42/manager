package com.hk.mgmt.dto.menu;

import java.util.List;

public record ModuleMenuDto(
        String id,
        String name,
        String icon,
        String url,
        MenuPermission permissions,
        List<MenuItemDto> menus
) {}