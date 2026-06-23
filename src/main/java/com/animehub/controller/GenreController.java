package com.animehub.controller;

import com.animehub.service.JikanApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class GenreController {

    /**
     * Common genre name -> Jikan genre ID mapping (see https://docs.api.jikan.moe
     * /v4/genres/anime for the full list). We expose a curated subset for a
     * clean nav instead of all 70+ genres Jikan supports.
     */
    public static final Map<String, Integer> GENRES = new LinkedHashMap<>();
    static {
        GENRES.put("Action", 1);
        GENRES.put("Adventure", 2);
        GENRES.put("Comedy", 4);
        GENRES.put("Drama", 8);
        GENRES.put("Fantasy", 10);
        GENRES.put("Horror", 14);
        GENRES.put("Mystery", 7);
        GENRES.put("Romance", 22);
        GENRES.put("Sci-Fi", 24);
        GENRES.put("Slice of Life", 36);
        GENRES.put("Sports", 30);
        GENRES.put("Supernatural", 37);
    }

    private final JikanApiService jikanApiService;

    public GenreController(JikanApiService jikanApiService) {
        this.jikanApiService = jikanApiService;
    }

    @GetMapping("/genre/{name}")
    public String genrePage(@PathVariable String name,
                             @RequestParam(defaultValue = "1") int page,
                             Model model) {
        Integer genreId = GENRES.get(name);
        model.addAttribute("genreName", name);
        model.addAttribute("genres", GENRES.keySet());
        model.addAttribute("currentPage", page);

        if (genreId == null) {
            model.addAttribute("animeList", java.util.Collections.emptyList());
        } else {
            model.addAttribute("animeList", jikanApiService.getAnimeByGenre(genreId, page));
        }
        return "genre";
    }
}
