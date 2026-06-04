package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_sys_role_setting", schema = "manager",
        indexes = @Index(name = "tbl_sys_role_setting_roleid_idx", columnList = "roleid"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleSetting {

    @Id
    @Column(length = 45)
    private String id;

    @Column(name = "roleId", nullable = false, length = 45)
    private String roleId;

    @Column(name = "moduleId", nullable = false, length = 45)
    private String moduleId;

    // null이면 모듈 전체에 대한 권한, 값이 있으면 특정 메뉴에 대한 권한
    @Column(name = "menuId", length = 45)
    private String menuId;

    @Column(nullable = false, length = 5)
    private String admin;

    @Column(nullable = false, length = 5)
    private String list;

    @Column(nullable = false, length = 5)
    private String read;

    @Column(nullable = false, length = 5)
    private String write;

    @Column(nullable = false)
    private Integer pIndex;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    private RoleSetting(String roleId, String moduleId, String menuId,
                        String admin, String list, String read, String write, Integer pIndex) {
        this.roleId = roleId;
        this.moduleId = moduleId;
        this.menuId = menuId;
        this.admin = admin;
        this.list = list;
        this.read = read;
        this.write = write;
        this.pIndex = pIndex;
    }

    public static RoleSetting create(String roleId, String moduleId, String menuId,
                                     String admin, String list, String read, String write) {
        return RoleSetting.builder()
                .roleId(roleId).moduleId(moduleId).menuId(menuId)
                .admin(admin).list(list).read(read).write(write)
                .pIndex(1)
                .build();
    }

    public boolean hasListPermission()  { return "Y".equals(list); }
    public boolean hasReadPermission()  { return "Y".equals(read); }
    public boolean hasWritePermission() { return "Y".equals(write); }
    public boolean hasAdminPermission() { return "Y".equals(admin); }
}