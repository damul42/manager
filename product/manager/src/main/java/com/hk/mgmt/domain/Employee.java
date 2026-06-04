package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "tbl_biz_employee", schema = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    private static final DateTimeFormatter AUDIT_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Id
    @Column(length = 45)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String employeeNo;      // 사번

    @Column(nullable = false, length = 100)
    private String name;            // 이름

    @Column(length = 45)
    private String orgId;           // 부서 ID

    @Column(length = 100)
    private String position;        // 직위

    @Column(length = 20)
    private String mobile;          // 휴대폰

    @Column(length = 20)
    private String phone;           // 유선전화

    @Column(length = 100)
    private String email;           // 업무 이메일 (참조용, 로그인 아님)

    @Column(length = 250)
    private String photo;           // 사진 경로

    @Column(nullable = false, length = 8)
    private String startDate;       // 입사일 (YYYYMMDD)

    @Column(length = 8)
    private String endDate;         // 퇴사일 (YYYYMMDD)

    @Column(nullable = false, length = 45)
    private String status;          // E=재직, D=퇴직

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

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
        if (this.id == null) this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    private Employee(String employeeNo, String name, String orgId, String position,
                     String mobile, String phone, String email, String photo,
                     String startDate, String endDate, String status, String regId) {
        this.employeeNo = employeeNo;
        this.name = name;
        this.orgId = orgId;
        this.position = position;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status != null ? status : "E";
        this.regId = regId;
        this.regDate = LocalDateTime.now().format(AUDIT_FMT);
    }

    /**
     * 변경 가능한 필드 일괄 수정.
     * 호출 전 EmployeeHistory.of(this, ...) 로 이력을 먼저 저장해야 한다.
     */
    public void update(String name, String orgId, String position,
                       String mobile, String phone, String email,
                       String photo, String endDate, String status, String uptId) {
        this.name = name;
        this.orgId = orgId;
        this.position = position;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.endDate = endDate;
        this.status = status;
        this.uptId = uptId;
        this.uptDate = LocalDateTime.now().format(AUDIT_FMT);
    }

    public void changeStatus(String status) {
        this.status = status;
        this.uptDate = LocalDateTime.now().format(AUDIT_FMT);
    }
}