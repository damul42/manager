package com.hk.mgmt.repository;

import com.hk.mgmt.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    // LEFT JOIN FETCH 명시 — @EntityGraph derived query는 Hibernate 6에서 INNER JOIN 생성 가능
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.employee ORDER BY u.createdAt DESC")
    List<User> findAllWithEmployee();

    @EntityGraph(attributePaths = "employee")
    Optional<User> findWithEmployeeById(String id);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.employee emp
            WHERE (:email  IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:email AS String), '%')))
              AND (:name   IS NULL OR LOWER(COALESCE(emp.name, u.name, ''))
                                      LIKE LOWER(CONCAT('%', CAST(:name AS String), '%')))
              AND (:status IS NULL OR u.status = CAST(:status AS String))
              AND (CAST(:hasEmployee AS String) IS NULL
                   OR (CAST(:hasEmployee AS String) = 'Y' AND u.employee IS NOT NULL)
                   OR (CAST(:hasEmployee AS String) = 'N' AND u.employee IS NULL))
              AND (CAST(:dateFrom AS LocalDateTime) IS NULL OR u.createdAt >= :dateFrom)
              AND (CAST(:dateTo   AS LocalDateTime) IS NULL OR u.createdAt <= :dateTo)
            ORDER BY u.createdAt DESC
            """)
    List<User> search(@Param("email")       String email,
                      @Param("name")        String name,
                      @Param("status")      String status,
                      @Param("hasEmployee") String hasEmployee,
                      @Param("dateFrom")    LocalDateTime dateFrom,
                      @Param("dateTo")      LocalDateTime dateTo);

    @Query("SELECT u.employee.id FROM User u WHERE u.employee IS NOT NULL")
    List<String> findAllLinkedEmployeeIds();

    @Query("SELECT u FROM User u WHERE u.employee.id = :employeeId")
    Optional<User> findByEmployeeId(@Param("employeeId") String employeeId);
}