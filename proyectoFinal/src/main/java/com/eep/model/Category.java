package com.eep.model;

import java.util.List;

public class Category {
    private String name;
    private List<Game> games;

    public Category() {}

    public Category(String name, List<Game> games) {
        this.name = name;
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}