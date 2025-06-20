package com.eep.service;

import java.util.Collections;
import java.util.List;

import com.eep.dto.NewsDtos.Article;
import com.eep.dto.NewsDtos.NewsApiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    @Value("${newsapi.url}")
    private String baseUrl;       

    @Value("${newsapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Article> getLatestNews() {
        String url = String.format("%s&apiKey=%s", baseUrl, apiKey);
        NewsApiResponse resp = restTemplate.getForObject(url, NewsApiResponse.class);
        if (resp != null && "ok".equalsIgnoreCase(resp.getStatus())) {
            return resp.getArticles();
        }
        return Collections.emptyList();
    }
}
