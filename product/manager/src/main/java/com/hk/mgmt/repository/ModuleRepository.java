package com.hk.mgmt.repository;

import com.hk.mgmt.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByStatusOrderBySeqAsc(String status);
    List<Module> findAllByOrderBySeqAsc();
    Optional<Module> findByName(String name);
}