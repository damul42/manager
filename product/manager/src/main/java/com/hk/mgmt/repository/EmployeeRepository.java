package com.hk.mgmt.repository;

import com.hk.mgmt.domain.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmployeeNo(String employeeNo);
    Optional<Employee> findByEmail(String email);
    boolean existsByEmployeeNo(String employeeNo);
    List<Employee> findAllByOrderByEmployeeNoAsc();

    @Query("""
            SELECT e FROM Employee e
            WHERE e.status = 'E'
              AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%'))
                OR e.employeeNo  LIKE       CONCAT('%', :q, '%'))
            ORDER BY e.employeeNo ASC
            """)
    List<Employee> searchActive(@Param("q") String q, Pageable pageable);

    @Query("""
            SELECT e FROM Employee e
            WHERE (:status IS NULL OR e.status = :status)
              AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:employeeNo IS NULL OR e.employeeNo LIKE CONCAT('%', :employeeNo, '%'))
              AND (:orgId IS NULL OR LOWER(COALESCE(e.orgId,'')) LIKE LOWER(CONCAT('%', :orgId, '%')))
            ORDER BY e.employeeNo ASC
            """)
    List<Employee> search(@Param("status") String status,
                          @Param("name") String name,
                          @Param("employeeNo") String employeeNo,
                          @Param("orgId") String orgId);
}