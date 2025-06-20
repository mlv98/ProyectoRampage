package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RawgGameDto(
    Long id,
    String slug,
    String name,
    @JsonProperty("background_image") String backgroundImage,
    @JsonProperty("released") String released,
    List<PlatformWrapper> platforms
) {
    public List<String> getPlatforms() {
        return platforms.stream()
                         .map(p -> p.platform().name())
                         .toList();
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getSlug() {
        return slug;
    }

    public String getReleased() {
        return released;
    }

    public record PlatformWrapper(Platform platform) {}
    public record Platform(String name) {}
}
