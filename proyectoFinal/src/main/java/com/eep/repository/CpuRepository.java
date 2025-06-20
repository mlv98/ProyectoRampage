package com.eep.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eep.model.Cpu;

public interface CpuRepository extends JpaRepository<Cpu, Long> {
    List<Cpu> findByNameContainingIgnoreCase(String name);
}
