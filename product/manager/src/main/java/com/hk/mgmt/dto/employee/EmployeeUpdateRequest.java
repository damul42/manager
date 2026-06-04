package com.hk.mgmt.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeUpdateRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 45)  String orgId,
        @Size(max = 100) String position,
        @Size(max = 20)  String mobile,
        @Size(max = 20)  String phone,
        @Size(max = 100) String email,
        @Size(max = 250) String photo,
        @Size(max = 8)   String endDate,
        String status
) {}