// src/main/java/com/eep/dto/SystemSpec.java
package com.eep.dto;

public class SystemSpec {

    private Long    cpuId;
    private String  cpuName;
    private Long    gpuId;
    private String  gpuName;
    private Integer ram;
    private Integer storage;
    private String  gameSlug;


    public SystemSpec() {
    }

   
    public SystemSpec(String cpuName, String gpuName, Integer ram, Integer storage, String gameSlug) {
        this.cpuName  = cpuName;
        this.gpuName  = gpuName;
        this.ram      = ram;
        this.storage  = storage;
        this.gameSlug = gameSlug;
    }

    
    public SystemSpec(String cpuName, String gpuName, Integer ram, Integer storage) {
        this(cpuName, gpuName, ram, storage, null);
    }

    // Getters y setters

    public Long getCpuId() {
        return cpuId;
    }

    public void setCpuId(Long cpuId) {
        this.cpuId = cpuId;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public Long getGpuId() {
        return gpuId;
    }

    public void setGpuId(Long gpuId) {
        this.gpuId = gpuId;
    }

    public String getGpuName() {
        return gpuName;
    }

    public void setGpuName(String gpuName) {
        this.gpuName = gpuName;
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

    public String getGameSlug() {
        return gameSlug;
    }

    public void setGameSlug(String gameSlug) {
        this.gameSlug = gameSlug;
    }
}
