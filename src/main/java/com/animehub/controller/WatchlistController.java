package com.animehub.controller;

import com.animehub.model.WatchStatus;
import com.animehub.service.WatchlistService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    // Note: lahat ng routes dito ay protected na ng SecurityConfig
    // (.requestMatchers("/watchlist/**").authenticated()), kaya garantisadong
    // may naka-login na user bago pa man pumasok dito ang request.

    @GetMapping
    public String viewWatchlist(Authentication auth, Model model) {
        model.addAttribute("items", watchlistService.getWatchlistForUser(auth.getName()));
        model.addAttribute("statuses", WatchStatus.values());
        return "watchlist";
    }

    @PostMapping("/add")
    public String add(@RequestParam Integer malId,
                       @RequestParam String title,
                       @RequestParam(required = false) String imageUrl,
                       @RequestParam(required = false) String redirectTo,
                       Authentication auth) {
        watchlistService.addToWatchlist(auth.getName(), malId, title, imageUrl);
        return "redirect:" + (redirectTo != null ? redirectTo : "/anime/" + malId);
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Integer malId,
                          @RequestParam(required = false) String redirectTo,
                          Authentication auth) {
        watchlistService.removeFromWatchlist(auth.getName(), malId);
        return "redirect:" + (redirectTo != null ? redirectTo : "/watchlist");
    }

    @PostMapping("/status")
    public String updateStatus(@RequestParam Integer malId,
                                @RequestParam WatchStatus status,
                                Authentication auth) {
        watchlistService.updateStatus(auth.getName(), malId, status);
        return "redirect:/watchlist";
    }
}
