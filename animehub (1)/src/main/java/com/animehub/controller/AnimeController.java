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

        // If logged in, tell the template whether this is already on the
        // watchlist (and at what episode) so it can show "Add" vs "Remove"
        // plus the episode progress tracker.
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            boolean inWatchlist = watchlistService.isInWatchlist(auth.getName(), malId);
            model.addAttribute("inWatchlist", inWatchlist);
            if (inWatchlist) {
                watchlistService.getWatchlistForUser(auth.getName()).stream()
                        .filter(item -> item.getAnimeMalId().equals(malId))
                        .findFirst()
                        .ifPresent(item -> model.addAttribute("episodeProgress", item.getEpisodeProgress()));
            }
        }
        return "anime-detail";
    }
}
