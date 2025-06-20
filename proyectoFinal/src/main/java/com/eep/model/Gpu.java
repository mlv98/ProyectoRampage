// src/main/java/com/eep/model/Gpu.java
package com.eep.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gpu")
public class Gpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "gpuName")
    private String name;
    
    @Column(name = "G3Dmark")
    private Integer g3dMark;
    
    private String gpuLog;

    // getters & setters
    
    
    
    public Long getId() {
        return id;
    }
    public String getGpuLog() {
		return gpuLog;
	}
	public void setGpuLog(String gpuLog) {
		this.gpuLog = gpuLog;
	}
	public String getName() {
        return name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
	public Integer getG3dMark() {
		return g3dMark;
	}
	public void setG3dMark(Integer g3dMark) {
		this.g3dMark = g3dMark;
	}
    
}
