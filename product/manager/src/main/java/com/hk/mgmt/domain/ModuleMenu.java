package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_sys_module_menu", schema = "manager",
        indexes = @Index(name = "tbl_sys_module_menu_moduleid_idx", columnList = "moduleid"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModuleMenu {

    @Id
    @Column(length = 45)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moduleId", nullable = false)
    private Module module;

    private Integer seq;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String icon;

    @Column(length = 5)
    private String status;

    @Column(length = 255)
    private String url;

    @Column(length = 100)
    private String original;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Builder
    private ModuleMenu(Module module, Integer seq, String name, String icon,
                       String status, String url, String original) {
        this.module = module;
        this.seq = seq;
        this.name = name;
        this.icon = icon;
        this.status = status;
        this.url = url;
        this.original = original;
    }

    public static ModuleMenu create(Module module, Integer seq, String name,
                                    String icon, String url, String status) {
        return ModuleMenu.builder()
                .module(module)
                .seq(seq)
                .name(name)
                .icon(icon)
                .url(url)
                .status(status)
                .build();
    }
}