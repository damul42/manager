package com.hk.mgmt.repository;

import com.hk.mgmt.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findAllByUserId(String userId);
    List<UserRole> findAllByRoleId(String roleId);
    void deleteAllByUserId(String userId);
    void deleteAllByRoleId(String roleId);
}