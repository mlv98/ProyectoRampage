package com.eep.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eep.dto.RawgDtos;
import com.eep.model.Category;
import com.eep.model.Game;

@Service
public class GameService {

	@Value("${rawg.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate;
	private static final String BASE_URL = "https://api.rawg.io/api";

	public GameService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Obtiene categorías con sus juegos desde la API RAWG usando RestTemplate.
	 */
	public List<Category> fetchCategoriesWithGames() {
		String genresUrl = String.format("https://api.rawg.io/api/genres?key=%s", apiKey);
		RawgDtos.RawgGenresResponse genresRes = restTemplate.getForObject(genresUrl, RawgDtos.RawgGenresResponse.class);

		List<Category> categories = new ArrayList<>();
		if (genresRes != null && genresRes.results() != null) {
			for (RawgDtos.RawgGenre genre : genresRes.results()) {
				String gamesUrl = String.format("https://api.rawg.io/api/games?key=%s&genres=%s&page_size=10", apiKey,
						genre.slug());
				RawgDtos.RawgGamesResponse gamesRes = restTemplate.getForObject(gamesUrl,
						RawgDtos.RawgGamesResponse.class);

				List<Game> games = new ArrayList<>();
				if (gamesRes != null && gamesRes.results() != null) {
					for (RawgDtos.RawgGameDto dto : gamesRes.results()) {
						games.add(new Game(dto.id(), // id
								dto.name(), // nombre
								dto.getBackgroundImage(), // fondo
								dto.getSlug(), // slug cargado desde el DTO
								dto.getPlatforms() // plataformas
						));
					}
				}
				categories.add(new Category(genre.name(), games));
			}
		}
		return categories;
	}

	// Nuevo método para obtener un juego por ID:
	public Game findById(Long id) {
		String url = String.format("%s/games/%d?key=%s", BASE_URL, id, apiKey);
		RawgDtos.RawgGameDto dto = restTemplate.getForObject(url, RawgDtos.RawgGameDto.class);
		if (dto == null) {
			throw new IllegalArgumentException("Juego no encontrado: " + id);
		}
		return new Game(dto.id(), // id
				dto.name(), // nombre
				dto.getBackgroundImage(), // fondo
				dto.getSlug(), // slug cargado desde el DTO
				dto.getPlatforms() // plataformas
		);
	}

	/**
	 * Obtiene los últimos juegos desde la API RAWG.
	 * 
	 * @param pageSize número de juegos a recuperar
	 */
	public List<Game> fetchLatestGames(int count) {
		String url = String.format("%s/games?key=%s&ordering=-added&page_size=%d", BASE_URL, apiKey, count);
		RawgDtos.RawgGamesResponse resp = restTemplate.getForObject(url, RawgDtos.RawgGamesResponse.class);

		return resp.results().stream()
				.map(dto -> new Game(dto.id(), dto.name(), dto.getBackgroundImage(), dto.getSlug(), dto.getPlatforms(),
						// <--- aquí pasas el slug
						dto.getReleased() // <--- y aquí la fecha
				)).toList();
	}

}
