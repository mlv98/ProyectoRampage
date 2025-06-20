package com.eep.repository;

import com.eep.model.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GpuRepository extends JpaRepository<Gpu, Long> {
    List<Gpu> findByNameContainingIgnoreCase(String name);
}
