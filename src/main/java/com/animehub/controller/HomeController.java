package com.animehub.controller;

import com.animehub.service.JikanApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final JikanApiService jikanApiService;

    public HomeController(JikanApiService jikanApiService) {
        this.jikanApiService = jikanApiService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("latestAnime", jikanApiService.getLatestAnime(1));
        model.addAttribute("topAnimePreview", jikanApiService.getTopAnime(1).stream().limit(6).toList());
        return "index";
    }

    @GetMapping("/ranking")
    public String ranking(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("animeList", jikanApiService.getTopAnime(page));
        model.addAttribute("currentPage", page);
        return "ranking";
    }
}
