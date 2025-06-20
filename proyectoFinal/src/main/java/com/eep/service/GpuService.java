package com.eep.service;

import com.eep.model.Gpu;
import java.util.List;
import java.util.Optional;

public interface GpuService {
    List<Gpu> findAll();
    Optional<Gpu> findById(Long id);
}