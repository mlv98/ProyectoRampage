package com.eep.service.impl;

import com.eep.model.Cpu;
import com.eep.repository.CpuRepository;
import com.eep.service.CpuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CpuServiceImpl implements CpuService {

    private final CpuRepository repo;
    public CpuServiceImpl(CpuRepository repo) { this.repo = repo; }

    @Override public List<Cpu> findAll() { return repo.findAll(); }
    @Override public Optional<Cpu> findById(Long id) { return repo.findById(id); }
}