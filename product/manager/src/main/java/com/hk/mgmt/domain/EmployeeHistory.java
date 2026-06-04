package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Employee 변경 이력 — 변경 시점의 전체 스냅샷을 저장한다.
 * Employee.update() 호출 직전에 EmployeeHistory.of(employee, ...) 로 생성 후 저장해야 한다.
 */
@Entity
@Table(name = "tbl_biz_employee_history", schema = "manager",
        indexes = @Index(name = "idx_emp_history_employee_id", columnList = "employee_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeHistory {

    @Id
    @Column(length = 45)
    private String id;

    @Column(name = "employee_id", nullable = false, length = 45)
    private String employeeId;

    // 변경 직전 스냅샷
    private String employeeNo;
    private String name;
    private String orgId;
    private String position;
    private String mobile;
    private String phone;
    private String email;
    private String photo;
    private String startDate;
    private String endDate;
    private String status;

    @Column(nullable = false, length = 20)
    private String changeType;      // UPDATE, RETIRE 등

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(length = 45)
    private String changedBy;       // 변경자 user ID

    @PrePersist
    private void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

    public static EmployeeHistory of(Employee emp, String changeType, String changedBy) {
        EmployeeHistory h = new EmployeeHistory();
        h.employeeId = emp.getId();
        h.employeeNo = emp.getEmployeeNo();
        h.name       = emp.getName();
        h.orgId      = emp.getOrgId();
        h.position   = emp.getPosition();
        h.mobile     = emp.getMobile();
        h.phone      = emp.getPhone();
        h.email      = emp.getEmail();
        h.photo      = emp.getPhoto();
        h.startDate  = emp.getStartDate();
        h.endDate    = emp.getEndDate();
        h.status     = emp.getStatus();
        h.changeType = changeType;
        h.changedAt  = LocalDateTime.now();
        h.changedBy  = changedBy;
        return h;
    }
}