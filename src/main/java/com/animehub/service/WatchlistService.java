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
            // Idempotent - kung naka-add na, hindi error, tahimik lang itong i-ignore
            return;
        }
        watchlistRepository.save(new WatchlistItem(user, animeMalId, title, imageUrl));
    }

    @Transactional
    public void updateStatus(String username, Integer animeMalId, WatchStatus newStatus) {
        User user = getUserOrThrow(username);
        WatchlistItem item = watchlistRepository.findByUserAndAnimeMalId(user, animeMalId)
                .orElseThrow(() -> new IllegalArgumentException("Wala sa watchlist mo ang anime na ito."));
        item.setStatus(newStatus);
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
                .orElseThrow(() -> new IllegalStateException("Hindi nahanap ang naka-log-in na user."));
    }
}
