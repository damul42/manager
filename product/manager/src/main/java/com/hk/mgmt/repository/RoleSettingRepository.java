package com.hk.mgmt.repository;

import com.hk.mgmt.domain.RoleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleSettingRepository extends JpaRepository<RoleSetting, String> {

    List<RoleSetting> findAllByRoleId(String roleId);
    List<RoleSetting> findAllByRoleIdIn(List<String> roleIds);

    @Modifying
    @Query("DELETE FROM RoleSetting s WHERE s.roleId = :roleId")
    void deleteAllByRoleId(@Param("roleId") String roleId);

    // 동일 모듈+메뉴에 여러 Role이 겹칠 때 Union(높은 권한) 계산용
    @Query("""
            SELECT s FROM RoleSetting s
            WHERE s.roleId IN :roleIds
            ORDER BY s.moduleId, s.menuId NULLS LAST, s.pIndex ASC
            """)
    List<RoleSetting> findAllByRoleIdsOrdered(@Param("roleIds") List<String> roleIds);
}