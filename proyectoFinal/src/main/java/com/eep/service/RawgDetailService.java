package com.eep.service;

import com.eep.dto.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RawgDetailService {

    private final WebClient client;
    private final String apiKey;

    public RawgDetailService(WebClient rawgWebClient,
                             @Value("${rawg.api.key}") String apiKey) {
        this.client = rawgWebClient;
        this.apiKey = apiKey;
    }

    @Cacheable("gameDetails")
    public Mono<GameDetailDto> fetchFullDetails(String slug) {
        Mono<RawgDetailDto> detail = client.get()
            .uri(uri -> uri.path("/games/{slug}")
                           .queryParam("key", apiKey)
                           .build(slug))
            .retrieve().bodyToMono(RawgDetailDto.class);

        Mono<RawgListDto<ScreenshotDto>> shots = client.get()
            .uri(uri -> uri.path("/games/{slug}/screenshots")
                           .queryParam("key", apiKey)
                           .build(slug))
            .retrieve().bodyToMono(new ParameterizedTypeReference<RawgListDto<ScreenshotDto>>(){});

        Mono<RawgListDto<MovieDto>> movies = client.get()
            .uri(uri -> uri.path("/games/{slug}/movies")
                           .queryParam("key", apiKey)
                           .build(slug))
            .retrieve().bodyToMono(new ParameterizedTypeReference<RawgListDto<MovieDto>>(){});

        Mono<RawgListDto<RawgGameDto>> suggested = client.get()
    .uri(uri -> uri.path("/games/{slug}/suggested")
                   .queryParam("key", apiKey)
                   .build(slug))
    .retrieve()
    .bodyToMono(new ParameterizedTypeReference<RawgListDto<RawgGameDto>>() {})
    // Si falla la llamada a sugeridos, devolvemos lista vacía en lugar de error
    .onErrorReturn(new RawgListDto<>(List.of()));

        return Mono.zip(detail, shots, movies, suggested)
                   .map(tuple -> new GameDetailDto(
                       tuple.getT1(),
                       tuple.getT2().results(),
                       tuple.getT3().results(),
                       tuple.getT4().results()
                   ));
    }
}