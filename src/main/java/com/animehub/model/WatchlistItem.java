package com.animehub.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist_item", uniqueConstraints = {
        // Hindi pwedeng madoble ang parehong anime sa list ng parehong user
        @UniqueConstraint(columnNames = {"user_id", "anime_mal_id"})
})
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ID ng anime sa Jikan/MyAnimeList - ito ang "foreign key" papunta sa external API,
    // hindi natin kailangan kopyahin ang buong anime data sa sarili nating DB
    @Column(name = "anime_mal_id", nullable = false)
    private Integer animeMalId;

    @Column(nullable = false)
    private String animeTitle;

    private String animeImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WatchStatus status = WatchStatus.PLAN_TO_WATCH;

    @Column(nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    public WatchlistItem() {}

    public WatchlistItem(User user, Integer animeMalId, String animeTitle, String animeImageUrl) {
        this.user = user;
        this.animeMalId = animeMalId;
        this.animeTitle = animeTitle;
        this.animeImageUrl = animeImageUrl;
    }

    // ===== Getters and setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getAnimeMalId() { return animeMalId; }
    public void setAnimeMalId(Integer animeMalId) { this.animeMalId = animeMalId; }

    public String getAnimeTitle() { return animeTitle; }
    public void setAnimeTitle(String animeTitle) { this.animeTitle = animeTitle; }

    public String getAnimeImageUrl() { return animeImageUrl; }
    public void setAnimeImageUrl(String animeImageUrl) { this.animeImageUrl = animeImageUrl; }

    public WatchStatus getStatus() { return status; }
    public void setStatus(WatchStatus status) { this.status = status; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
