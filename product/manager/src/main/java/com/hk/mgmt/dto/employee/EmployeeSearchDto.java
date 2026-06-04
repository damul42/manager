package com.hk.mgmt.dto.employee;

import com.hk.mgmt.domain.Employee;

public record EmployeeSearchDto(
        String id,
        String employeeNo,
        String name,
        String orgId,
        String position
) {
    public static EmployeeSearchDto from(Employee e) {
        return new EmployeeSearchDto(e.getId(), e.getEmployeeNo(), e.getName(), e.getOrgId(), e.getPosition());
    }
}