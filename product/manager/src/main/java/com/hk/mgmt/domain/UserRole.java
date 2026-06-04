package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_sys_user_role", schema = "manager",
        indexes = @Index(name = "tbl_sys_user_role_userid_idx", columnList = "userid"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

    @Id
    @Column(length = 45)
    private String id;

    @Column(name = "userId", nullable = false, length = 45)
    private String userId;

    @Column(name = "roleId", nullable = false, length = 45)
    private String roleId;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    private UserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}