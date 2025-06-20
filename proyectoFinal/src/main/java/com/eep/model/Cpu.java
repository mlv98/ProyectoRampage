// src/main/java/com/eep/model/Cpu.java
package com.eep.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cpu")
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapea la columna cpuName de la tabla al campo name de la entidad
    @Column(name = "cpuName")
    private String name;
    
    @Column(name = "cpuMark")
    private Integer cpuMark;
    
    @Column(name = "cores")
    private Integer cores;
    
    @Column(name = "threadMark")
    private Integer threadMark;
    
    @Column(name = "tdp")
    private Integer tdp;

    // getters & setters
    
    
    public Long getId() {
        return id;
    }

    public Integer getCores() {
		return cores;
	}

	public void setCores(Integer cores) {
		this.cores = cores;
	}

	public Integer getThreadMark() {
		return threadMark;
	}

	public void setThreadMark(Integer threadMark) {
		this.threadMark = threadMark;
	}

	public Integer getTdp() {
		return tdp;
	}

	public void setTdp(Integer tdp) {
		this.tdp = tdp;
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
    public Integer getCpuMark() {
        return cpuMark;
    }

    public void setCpuMark(Integer cpuMark) {
        this.cpuMark = cpuMark;
    }
    

}
