// src/main/java/com/eep/exception/ResourceNotFoundException.java
package com.eep.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
