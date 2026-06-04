package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_sys_module", schema = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Module {

    @Id
    @Column(length = 45)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String remark;

    @Column(length = 5)
    private String status;

    private Integer seq;

    @Column(length = 100)
    private String icon;

    @Column(length = 255)
    private String url;

    @Column(length = 45)
    private String regId;

    @Column(length = 14)
    private String regDate;

    @Column(length = 45)
    private String uptId;

    @Column(length = 14)
    private String uptDate;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    @OrderBy("seq ASC")
    private List<ModuleMenu> menus = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    private Module(String name, String remark, String status, Integer seq, String icon, String url) {
        this.name = name;
        this.remark = remark;
        this.status = status;
        this.seq = seq;
        this.icon = icon;
        this.url = url;
    }

    // ── 팩토리 메서드 ──────────────────────────────────────────────────

    public static Module create(String name, String remark, String status, Integer seq, String icon, String url) {
        return Module.builder()
                .name(name)
                .remark(remark)
                .status(status)
                .seq(seq)
                .icon(icon)
                .url(url)
                .build();
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    public void update(String name, String remark, String status, Integer seq, String icon, String url) {
        this.name = name;
        this.remark = remark;
        this.status = status;
        this.seq = seq;
        this.icon = icon;
        this.url = url;
    }

    // ── 상태 변경 ─────────────────────────────────────────────────────

    public void changeStatus(String status) {
        this.status = status;
    }

    // ── 등록 메타 초기화 ───────────────────────────────────────────────

    public void initReg(String regDate) {
        this.regDate = regDate;
    }

    public void initUpt(String uptDate) {
        this.uptDate = uptDate;
    }
}