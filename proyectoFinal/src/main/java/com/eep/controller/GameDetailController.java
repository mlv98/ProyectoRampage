package com.eep.controller;

import com.eep.dto.GameDetailDto;
import com.eep.service.RawgDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/games")
public class GameDetailController {

    private final RawgDetailService detailService;
    private static final Logger log = LoggerFactory.getLogger(GameDetailController.class);

    public GameDetailController(RawgDetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/{slug}")
    public String showDetail(@PathVariable String slug, Model model) {
        GameDetailDto dto = detailService.fetchFullDetails(slug)
            .doOnError(ex -> log.error("Error llamando RAWG API", ex))
            .onErrorResume(ex -> Mono.just(GameDetailDto.empty(slug)))
            .block();

        model.addAttribute("game",        dto.detail());
        model.addAttribute("screenshots", dto.screenshots());
        model.addAttribute("movies",      dto.movies());
        model.addAttribute("suggested",   dto.suggested());
        return "detalles";
    }
}