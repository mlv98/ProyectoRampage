package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RawgDtos {

    public record RawgGenresResponse(List<RawgGenre> results) {}

    public record RawgGenre(String name, String slug) {}

    public record RawgGamesResponse(List<RawgGameDto> results) {}

    public record RawgGameDto(
        @JsonProperty("id") Long id,
        @JsonProperty("slug") String slug,
        @JsonProperty("name") String name,
        @JsonProperty("background_image") String backgroundImage,
        @JsonProperty("released") String released,
        @JsonProperty("platforms") List<PlatformWrapper> platforms
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
    }

    public record PlatformWrapper(Platform platform) {}
    public record Platform(String name) {}

}
