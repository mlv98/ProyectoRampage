package com.eep.service.impl;

import com.eep.model.Gpu;
import com.eep.repository.GpuRepository;
import com.eep.service.GpuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GpuServiceImpl implements GpuService {

    private final GpuRepository repo;
    public GpuServiceImpl(GpuRepository repo) { this.repo = repo; }

    @Override public List<Gpu> findAll() { return repo.findAll(); }
    @Override public Optional<Gpu> findById(Long id) { return repo.findById(id); }
}