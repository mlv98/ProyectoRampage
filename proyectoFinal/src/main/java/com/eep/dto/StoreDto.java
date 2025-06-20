package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StoreDto(
    Long id,
    String url,
    @JsonProperty("store") StoreDetail store
) {
    public record StoreDetail(
        Long id,
        String name,
        String slug,
        String domain
    ) {}
}