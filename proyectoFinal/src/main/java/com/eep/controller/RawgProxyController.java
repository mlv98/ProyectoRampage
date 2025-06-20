package com.eep.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class RawgProxyController {

    @Value("${rawg.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/games")
    public ResponseEntity<String> getGames(@RequestParam(defaultValue = "") String search) {
        try {
            String url = String.format(
                "https://api.rawg.io/api/games?key=%s&search=%s&page_size=12",
                apiKey, 
                search.replace(" ", "%20")
            );
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            return ResponseEntity.status(502).body("{\"error\": \"Error contactando a RAWG\"}");
        }
    }
}
