package com.eep.dto;

import lombok.Data;

@Data
public class RawgGameResponse {
    private String name;
    private Requirements requirements;

    @Data
    public static class Requirements {
        private String minimum;
        private String recommended;
    }
}
