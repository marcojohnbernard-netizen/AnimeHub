package com.animehub.controller;

import com.animehub.dto.AnimeDto;
import com.animehub.service.JikanApiService;
import com.animehub.service.WatchlistService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnimeController {

    private final JikanApiService jikanApiService;
    private final WatchlistService watchlistService;

    public AnimeController(JikanApiService jikanApiService, WatchlistService watchlistService) {
        this.jikanApiService = jikanApiService;
        this.watchlistService = watchlistService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                          @RequestParam(defaultValue = "1") int page,
                          Model model) {
        model.addAttribute("query", q);
        model.addAttribute("currentPage", page);
        model.addAttribute("results", (q == null || q.isBlank())
                ? java.util.Collections.<AnimeDto>emptyList()
                : jikanApiService.searchAnime(q, page));
        return "search";
    }

    @GetMapping("/anime/{malId}")
    public String detail(@PathVariable int malId, Model model, Authentication auth) {
        AnimeDto anime = jikanApiService.getAnimeDetail(malId);
        model.addAttribute("anime", anime);

        // Kung naka-login, sabihin sa template kung naka-add na sa watchlist
        // para malaman kung "Add" o "Remove" button ang ipapakita
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            boolean inWatchlist = watchlistService.isInWatchlist(auth.getName(), malId);
            model.addAttribute("inWatchlist", inWatchlist);
        }
        return "anime-detail";
    }
}
