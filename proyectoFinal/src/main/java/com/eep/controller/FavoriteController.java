package com.eep.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eep.model.Game;
import com.eep.service.FavoriteService;

@Controller
public class FavoriteController {

    private final FavoriteService favService;

    public FavoriteController(FavoriteService favService) {
        this.favService = favService;
    }

    @PostMapping("/favorites/add")
    @ResponseBody
    public ResponseEntity<Void> addFavorite(@RequestParam Long gameId, Principal principal) {
        favService.addFavorite(principal.getName(), gameId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorites/remove")
    @ResponseBody
    public ResponseEntity<Void> removeFavorite(@RequestParam Long gameId, Principal principal) {
        favService.removeFavorite(principal.getName(), gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model, Principal principal) {
        List<Game> favorites = favService.getFavorites(principal.getName());
        model.addAttribute("favorites", favorites);
        return "favorites";
    }
}