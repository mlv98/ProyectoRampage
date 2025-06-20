package com.eep.service;

import com.eep.model.Cpu;
import java.util.List;
import java.util.Optional;

public interface CpuService {
    List<Cpu> findAll();
    Optional<Cpu> findById(Long id);
}