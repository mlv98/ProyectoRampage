package com.eep.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.eep.model.Category;
import com.eep.model.Game;
import com.eep.service.FavoriteService;
import com.eep.service.GameService;

@Controller
@RequestMapping("/games")
public class GamesController {
  private final GameService gameService;
  private final FavoriteService favoriteService;

  public GamesController(GameService gs, FavoriteService fs) {
    this.gameService = gs;
    this.favoriteService = fs;
  }

  // Captura GET /games **y** /games/
  @GetMapping({"", "/"})
  public String showGames(Model model, Principal user) {
    var cats = gameService.fetchCategoriesWithGames();
    model.addAttribute("categories", cats);
    var favs = favoriteService.getFavorites(user.getName())
                  .stream().map(Game::getId).toList();
    model.addAttribute("favoriteIds", favs);
    return "games";
  }
}