package com.hk.mgmt.controller.api;

import com.hk.mgmt.dto.employee.EmployeeSearchDto;
import com.hk.mgmt.domain.Employee;
import com.hk.mgmt.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeApiController {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<EmployeeSearchDto>> search(
            @RequestParam(required = false, defaultValue = "") String q) {

        if (!StringUtils.hasText(q)) {
            return ResponseEntity.ok(List.of());
        }

        List<Employee> results = employeeRepository.searchActive(q, PageRequest.of(0, 20));
        return ResponseEntity.ok(results.stream().map(EmployeeSearchDto::from).toList());
    }
}