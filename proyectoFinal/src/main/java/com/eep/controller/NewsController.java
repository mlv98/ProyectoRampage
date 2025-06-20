package com.eep.controller;

import com.eep.dto.NewsDtos.Article;
import com.eep.service.NewsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String showNews(Model model) {
        List<Article> articles = newsService.getLatestNews();
        model.addAttribute("articles", articles);
        return "news";
    }
}
