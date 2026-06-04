package com.hk.mgmt.repository;

import com.hk.mgmt.domain.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, String> {
    List<EmployeeHistory> findAllByEmployeeIdOrderByChangedAtDesc(String employeeId);
}