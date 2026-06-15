package com.hk.mgmt.repository;

import com.hk.mgmt.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByStatusOrderBySeqAsc(String status);
    List<Module> findAllByOrderBySeqAsc();
    Optional<Module> findByName(String name);

    @Query("""
            SELECT m FROM Module m
            WHERE (:name   IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', CAST(:name AS String), '%')))
              AND (:status IS NULL OR m.status = CAST(:status AS String))
            ORDER BY m.seq ASC
            """)
    List<Module> search(@Param("name")   String name,
                        @Param("status") String status);
}