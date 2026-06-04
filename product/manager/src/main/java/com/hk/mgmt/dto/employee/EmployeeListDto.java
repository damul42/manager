package com.hk.mgmt.dto.employee;

import com.hk.mgmt.domain.Employee;

public record EmployeeListDto(
        String  id,
        String  employeeNo,
        String  name,
        String  orgId,
        String  position,
        String  status,
        String  startDate,
        String  endDate,
        boolean hasUser
) {
    public static EmployeeListDto from(Employee e, boolean hasUser) {
        return new EmployeeListDto(
                e.getId(), e.getEmployeeNo(), e.getName(),
                e.getOrgId(), e.getPosition(), e.getStatus(),
                e.getStartDate(), e.getEndDate(), hasUser
        );
    }
}