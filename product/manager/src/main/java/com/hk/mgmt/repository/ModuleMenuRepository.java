package com.hk.mgmt.repository;

import com.hk.mgmt.domain.ModuleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleMenuRepository extends JpaRepository<ModuleMenu, String> {

    List<ModuleMenu> findAllByModule_IdAndStatusOrderBySeqAsc(String moduleId, String status);

    @Query("SELECT DISTINCT m.module.id FROM ModuleMenu m")
    List<String> findModuleIdsHavingMenus();

    @Modifying
    @Query("DELETE FROM ModuleMenu m WHERE m.module.id = :moduleId")
    void deleteAllByModuleId(@Param("moduleId") String moduleId);
}