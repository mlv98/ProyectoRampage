package com.eep.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eep.model.Favorite;
import com.eep.model.Game;
import com.eep.repository.FavoriteRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository repo;
    private final GameService gameService;

    public FavoriteService(FavoriteRepository repo, GameService gameService) {
        this.repo = repo;
        this.gameService = gameService;
    }

    public void addFavorite(String username, Long gameId) {
        boolean exists = repo.findByUsername(username)
                             .stream()
                            
                             .anyMatch(fav -> Objects.equals(fav.getGameId(), gameId));
        if (!exists) {
            repo.save(new Favorite(username, gameId));
        }
    }

    @Transactional
    public void removeFavorite(String username, Long gameId) {
        repo.deleteByUsernameAndGameId(username, gameId);
    }

    public List<Game> getFavorites(String username) {
        return repo.findByUsername(username)
                   .stream()
                   .map(fav -> gameService.findById(fav.getGameId()))
                   .collect(Collectors.toList());
    }
}
