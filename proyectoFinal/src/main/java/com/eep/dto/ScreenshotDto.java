package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScreenshotDto(
    Long id,
    @JsonProperty("image") String imageUrl
) {}