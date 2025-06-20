package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieDto(
    Long id,
    @JsonProperty("data") MovieData data
) {
    public record MovieData(String max) {}
}