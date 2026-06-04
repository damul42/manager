package com.hk.mgmt.dto.menu;

public record MenuPermission(
        boolean admin,
        boolean list,
        boolean read,
        boolean write
) {
    public static MenuPermission none() {
        return new MenuPermission(false, false, false, false);
    }

    public MenuPermission merge(MenuPermission other) {
        return new MenuPermission(
                this.admin || other.admin,
                this.list  || other.list,
                this.read  || other.read,
                this.write || other.write
        );
    }
}