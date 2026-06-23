package com.animehub.service;

import com.animehub.model.User;
import com.animehub.model.WatchStatus;
import com.animehub.model.WatchlistItem;
import com.animehub.repository.UserRepository;
import com.animehub.repository.WatchlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;

    public WatchlistService(WatchlistRepository watchlistRepository, UserRepository userRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
    }

    public List<WatchlistItem> getWatchlistForUser(String username) {
        User user = getUserOrThrow(username);
        return watchlistRepository.findByUserOrderByAddedAtDesc(user);
    }

    @Transactional
    public void addToWatchlist(String username, Integer animeMalId, String title, String imageUrl) {
        User user = getUserOrThrow(username);

        if (watchlistRepository.existsByUserAndAnimeMalId(user, animeMalId)) {
            // Idempotent - if already added, silently ignore instead of erroring
            return;
        }
        watchlistRepository.save(new WatchlistItem(user, animeMalId, title, imageUrl));
    }

    @Transactional
    public void updateStatus(String username, Integer animeMalId, WatchStatus newStatus) {
        User user = getUserOrThrow(username);
        WatchlistItem item = watchlistRepository.findByUserAndAnimeMalId(user, animeMalId)
                .orElseThrow(() -> new IllegalArgumentException("This anime isn't in your list."));
        item.setStatus(newStatus);
        watchlistRepository.save(item);
    }

    /**
     * Updates how many episodes the user has tracked as watched. This is a
     * manual progress tracker (like MyAnimeList/AniList) - not a video
     * player, so episodeCount is clamped to be non-negative only; we don't
     * validate against the anime's real episode total here since that would
     * require an extra API call per update.
     */
    @Transactional
    public void updateEpisodeProgress(String username, Integer animeMalId, int episodeCount) {
        User user = getUserOrThrow(username);
        WatchlistItem item = watchlistRepository.findByUserAndAnimeMalId(user, animeMalId)
                .orElseThrow(() -> new IllegalArgumentException("This anime isn't in your list."));
        item.setEpisodeProgress(Math.max(0, episodeCount));
        // Mark as Watching automatically once progress starts, unless already completed/dropped
        if (item.getStatus() == WatchStatus.PLAN_TO_WATCH && episodeCount > 0) {
            item.setStatus(WatchStatus.WATCHING);
        }
        watchlistRepository.save(item);
    }

    @Transactional
    public void removeFromWatchlist(String username, Integer animeMalId) {
        User user = getUserOrThrow(username);
        watchlistRepository.deleteByUserAndAnimeMalId(user, animeMalId);
    }

    public boolean isInWatchlist(String username, Integer animeMalId) {
        User user = getUserOrThrow(username);
        return watchlistRepository.existsByUserAndAnimeMalId(user, animeMalId);
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Could not find the logged-in user."));
    }
}
