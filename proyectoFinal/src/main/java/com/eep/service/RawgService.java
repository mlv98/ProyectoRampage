// src/main/java/com/eep/service/RawgService.java
package com.eep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RawgService {
    private final WebClient client;
    @Value("${rawg.api.key}")
    private String apiKey;

    public RawgService(WebClient rawgWebClient) {
        this.client = rawgWebClient;
    }

    public Mono<RawgSearchResult> searchGames(String query) {
        return client.get()
            .uri(uri -> uri
                .path("/games")
                .queryParam("search", query)
                .queryParam("key", apiKey)
                .build())
            .retrieve()
            .bodyToMono(RawgSearchResult.class);
    }

    public Mono<RawgGameInfo> fetchGameInfo(String slug) {
        return client.get()
            .uri(uri -> uri
                .path("/games/{slug}")
                .queryParam("key", apiKey)
                .build(slug))
            .retrieve()
            .bodyToMono(RawgGameInfo.class);
    }

    // Clases internas para mapear la respuesta
    public static record RawgSearchResult(int count, List<SearchGame> results) {}
    public static record SearchGame(String name, String slug) {}
    public static record RawgGameInfo(
        String name,
        List<PlatformInfo> platforms
    ) {}
    public static record PlatformInfo(Platform platform, Requirements requirements) {}
    public static record Platform(String name) {}
    public static record Requirements(String minimum, String recommended) {}
}
