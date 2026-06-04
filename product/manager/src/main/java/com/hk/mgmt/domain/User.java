package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_biz_user", schema = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(length = 45)
    private String id;

    /**
     * 연결된 직원 정보 (nullable).
     * 직원이 아닌 외부 계정은 null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false, unique = true, length = 100)
    private String email;           // 로그인 ID

    /**
     * 표시 이름 — employee 연동 시 employee.name 우선 사용.
     * 독립 계정의 경우 이 값을 사용.
     */
    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 5)
    private String status;          // E=활성, D=비활성

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean passwordChangeRequired;

    @PrePersist
    private void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "E";
    }

    @Builder
    private User(Employee employee, String email, String name, String password, String status,
                 boolean passwordChangeRequired) {
        this.employee               = employee;
        this.email                  = email;
        this.name                   = name;
        this.password               = password;
        this.status                 = status != null ? status : "E";
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public static User create(String email, String name, String encodedPassword, String status, Employee employee) {
        return User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .status(status)
                .employee(employee)
                .build();
    }

    /** 직원 등록 시 자동 생성 — 임시 비밀번호, 최초 로그인 시 변경 강제 */
    public static User createWithTempPassword(String email, String name, String encodedPassword, Employee employee) {
        return User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .status("E")
                .employee(employee)
                .passwordChangeRequired(true)
                .build();
    }

    public void clearPasswordChangeRequired() {
        this.passwordChangeRequired = false;
    }

    /** 활성 계정 여부 */
    public boolean isEnabled() {
        return "E".equals(status);
    }

    /**
     * 표시 이름 우선순위: employee.name → user.name → email
     */
    public String getDisplayName() {
        if (employee != null && employee.getName() != null) return employee.getName();
        return name != null ? name : email;
    }

    public void updateProfile(String name, String status) {
        this.name   = name;
        this.status = status;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeStatus(String status) {
        this.status = status;
    }
}