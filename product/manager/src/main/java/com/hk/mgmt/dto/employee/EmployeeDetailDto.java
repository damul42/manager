package com.hk.mgmt.dto.employee;

import com.hk.mgmt.domain.Employee;
import com.hk.mgmt.domain.User;

public record EmployeeDetailDto(
        String  id,
        String  employeeNo,
        String  name,
        String  orgId,
        String  position,
        String  mobile,
        String  phone,
        String  email,
        String  photo,
        String  startDate,
        String  endDate,
        String  status,
        String  regDate,
        String  uptDate,
        boolean hasUser,
        String  linkedUserId,
        String  linkedUserEmail
) {
    public static EmployeeDetailDto from(Employee e, User linkedUser) {
        return new EmployeeDetailDto(
                e.getId(), e.getEmployeeNo(), e.getName(),
                e.getOrgId(), e.getPosition(),
                e.getMobile(), e.getPhone(), e.getEmail(), e.getPhoto(),
                e.getStartDate(), e.getEndDate(), e.getStatus(),
                e.getRegDate(), e.getUptDate(),
                linkedUser != null,
                linkedUser != null ? linkedUser.getId() : null,
                linkedUser != null ? linkedUser.getEmail() : null
        );
    }
}