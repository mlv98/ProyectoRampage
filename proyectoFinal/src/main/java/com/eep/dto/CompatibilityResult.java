// src/main/java/com/eep/dto/CompatibilityResult.java
package com.eep.dto;

import java.util.ArrayList;
import java.util.List;

public class CompatibilityResult {
    private String message;
    private List<String> issues = new ArrayList<>();

   
   private String cpuLog;
   
   private String gpuLog;
   
   
   

   public String getGpuLog() {
	return gpuLog;
}
public void setGpuLog(String gpuLog) {
	this.gpuLog = gpuLog;
}
public String getCpuLog() {
       return cpuLog;
   }
   public void setCpuLog(String cpuLog) {
       this.cpuLog = cpuLog;
   }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<String> getIssues() {
        return issues;
    }
    public void addIssue(String issue) {
        this.issues.add(issue);
    }
}
