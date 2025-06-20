package com.eep.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_requirements")
public class GameRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameName;
    private String minCpu;
    private String minGpu;
    private String minRam;
    private String recCpu;
    private String recGpu;
    private String recRam;

    public GameRequirement() {}

    public GameRequirement(String gameName,
                           String minCpu, String minGpu, String minRam,
                           String recCpu, String recGpu, String recRam) {
        this.gameName = gameName;
        this.minCpu   = minCpu;
        this.minGpu   = minGpu;
        this.minRam   = minRam;
        this.recCpu   = recCpu;
        this.recGpu   = recGpu;
        this.recRam   = recRam;
    }

    public Long getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMinCpu() {
        return minCpu;
    }
    public void setMinCpu(String minCpu) {
        this.minCpu = minCpu;
    }

    public String getMinGpu() {
        return minGpu;
    }
    public void setMinGpu(String minGpu) {
        this.minGpu = minGpu;
    }

    public String getMinRam() {
        return minRam;
    }
    public void setMinRam(String minRam) {
        this.minRam = minRam;
    }

    public String getRecCpu() {
        return recCpu;
    }
    public void setRecCpu(String recCpu) {
        this.recCpu = recCpu;
    }

    public String getRecGpu() {
        return recGpu;
    }
    public void setRecGpu(String recGpu) {
        this.recGpu = recGpu;
    }

    public String getRecRam() {
        return recRam;
    }
    public void setRecRam(String recRam) {
        this.recRam = recRam;
    }
}
