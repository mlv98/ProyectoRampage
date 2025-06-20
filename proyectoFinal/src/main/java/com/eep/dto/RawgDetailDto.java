package com.eep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RawgDetailDto(
    Long id,
    String slug,
    String name,
    @JsonProperty("description") String descriptionHtml,
    @JsonProperty("description_raw") String descriptionRaw,
    String released,
    String updated,
    @JsonProperty("background_image") String backgroundImage,
    @JsonProperty("background_image_additional") String backgroundImageAdditional,
    String website,
    Double rating,
    Integer metacritic,
    @JsonProperty("playtime") Integer playtime,
    @JsonProperty("screenshots_count") Integer screenshotsCount,
    @JsonProperty("movies_count") Integer moviesCount,
    @JsonProperty("added_by_status") Object addedByStatus,
    List<ParentPlatformDto> parentPlatforms,
    List<StoreDto> stores
) {}