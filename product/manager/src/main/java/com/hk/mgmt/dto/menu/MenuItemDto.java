package com.hk.mgmt.dto.menu;

public record MenuItemDto(
        String id,
        String name,
        String icon,
        String url,
        MenuPermission permissions
) {}