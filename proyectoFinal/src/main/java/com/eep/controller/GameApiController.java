package com.eep.controller;

import com.eep.dto.GameDetailDto;
import com.eep.service.RawgDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/games")
public class GameApiController {

    private final RawgDetailService detailService;

    public GameApiController(RawgDetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/{slug}")
    public Mono<GameDetailDto> getGameDetail(@PathVariable String slug) {
        return detailService.fetchFullDetails(slug)
            .onErrorResume(ex -> Mono.just(GameDetailDto.empty(slug)));
    }
}