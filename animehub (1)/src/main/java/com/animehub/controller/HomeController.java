package com.animehub.controller;

import com.animehub.service.JikanApiService;
import com.animehub.service.WatchlistService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final JikanApiService jikanApiService;
    private final WatchlistService watchlistService;

    public HomeController(JikanApiService jikanApiService, WatchlistService watchlistService) {
        this.jikanApiService = jikanApiService;
        this.watchlistService = watchlistService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        // Sidebar: Top 10 ranking
        model.addAttribute("topAnimePreview", jikanApiService.getTopAnime(1).stream().limit(10).toList());

        // Content rows
        model.addAttribute("trendingPreview", jikanApiService.getTrendingAnime(1).stream().limit(10).toList());
        model.addAttribute("actionPreview", jikanApiService.getAnimeByGenre(
                GenreController.GENRES.get("Action"), 1).stream().limit(10).toList());
        model.addAttribute("completePreview", jikanApiService.getCompleteAnime(1).stream().limit(10).toList());

        // Continue Watching - only meaningful for a logged-in user with items in their list
        boolean loggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn) {
            model.addAttribute("continueWatching", watchlistService.getWatchlistForUser(auth.getName())
                    .stream().limit(10).toList());
        }
        return "index";
    }

    @GetMapping("/latest")
    public String latest(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("animeList", jikanApiService.getLatestAnime(page));
        model.addAttribute("currentPage", page);
        return "latest";
    }

    @GetMapping("/ranking")
    public String ranking(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("animeList", jikanApiService.getTopAnime(page));
        model.addAttribute("currentPage", page);
        return "ranking";
    }

    @GetMapping("/trending")
    public String trending(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("animeList", jikanApiService.getTrendingAnime(page));
        model.addAttribute("currentPage", page);
        return "trending";
    }

    @GetMapping("/complete")
    public String complete(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("animeList", jikanApiService.getCompleteAnime(page));
        model.addAttribute("currentPage", page);
        return "complete";
    }
}
