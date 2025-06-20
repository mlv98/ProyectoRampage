package com.eep.dto;

import java.util.List;

public record GameDetailDto(
    RawgDetailDto detail,
    List<ScreenshotDto> screenshots,
    List<MovieDto> movies,
    List<RawgGameDto> suggested
) {
    public static GameDetailDto empty(String slug) {
        RawgDetailDto emptyDetail = new RawgDetailDto(
            null,
            slug,
            "No disponible",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0.0,
            0,
            0,
            0,
            0,
            null,
            List.of(),
            List.of()
        );
        return new GameDetailDto(emptyDetail, List.of(), List.of(), List.of());
    }
}