// src/main/java/com/eep/service/BenchmarkService.java
package com.eep.service;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BenchmarkService {
    private final JdbcTemplate jdbc;

    public BenchmarkService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Integer> getCpuMark(String cpuName) {
        String sql = "SELECT cpuMark FROM cpu_benchmarks WHERE cpuName = ?";
        try {
            Integer mark = jdbc.queryForObject(sql, Integer.class, cpuName);
            return Optional.ofNullable(mark);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getGpuMark(String gpuName) {
        String sql = "SELECT G3Dmark FROM gpu_benchmarks WHERE gpuName = ?";
        try {
            Integer mark = jdbc.queryForObject(sql, Integer.class, gpuName);
            return Optional.ofNullable(mark);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
