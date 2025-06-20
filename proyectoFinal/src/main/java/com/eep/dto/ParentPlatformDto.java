package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParentPlatformDto(
    @JsonProperty("platform") PlatformDetail platform
) {
    public record PlatformDetail(
        Long id,
        String name,
        String slug
    ) {}
}