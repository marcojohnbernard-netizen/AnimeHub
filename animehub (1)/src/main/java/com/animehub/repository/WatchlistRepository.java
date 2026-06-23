package com.animehub.repository;

import com.animehub.model.User;
import com.animehub.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUserOrderByAddedAtDesc(User user);
    Optional<WatchlistItem> findByUserAndAnimeMalId(User user, Integer animeMalId);
    boolean existsByUserAndAnimeMalId(User user, Integer animeMalId);
    void deleteByUserAndAnimeMalId(User user, Integer animeMalId);
}
