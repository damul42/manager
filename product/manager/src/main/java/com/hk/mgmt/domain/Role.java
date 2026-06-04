package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_sys_role", schema = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @Column(length = 45)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String remark;

    @Column(length = 45)
    private String regId;

    @Column(length = 14)
    private String regDate;

    @Column(length = 45)
    private String uptId;

    @Column(length = 14)
    private String uptDate;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    private Role(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }

    public static Role create(String name, String remark) {
        return Role.builder().name(name).remark(remark).build();
    }

    public void update(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }

    public void initReg(String regDate) { this.regDate = regDate; }
    public void initUpt(String uptDate) { this.uptDate = uptDate; }
}