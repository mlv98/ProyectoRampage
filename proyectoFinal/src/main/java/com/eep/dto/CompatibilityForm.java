// src/main/java/com/eep/dto/CompatibilityForm.java
package com.eep.dto;

public class CompatibilityForm {

    private String gameSlug;
    private Long cpuId;
    private Long gpuId;
    private Integer ram;
    private Integer storage;

    
    public CompatibilityForm() {
    }

    // Getters & setters

    public String getGameSlug() {
        return gameSlug;
    }

    public void setGameSlug(String gameSlug) {
        this.gameSlug = gameSlug;
    }

    public Long getCpuId() {
        return cpuId;
    }

    public void setCpuId(Long cpuId) {
        this.cpuId = cpuId;
    }

    public Long getGpuId() {
        return gpuId;
    }

    public void setGpuId(Long gpuId) {
        this.gpuId = gpuId;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }
}
