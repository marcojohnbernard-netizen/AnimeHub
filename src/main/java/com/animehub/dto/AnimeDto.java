package com.animehub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Eto ang shape ng "data" object na ibinabalik ng Jikan API kapag humingi
 * tayo ng info tungkol sa isang anime. @JsonIgnoreProperties(ignoreUnknown)
 * ay importante - marami pang fields ang Jikan na hindi natin ginagamit,
 * at ayaw nating bumagsak ang app kung magdagdag sila ng bagong field sa
 * future.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeDto {

    private Integer malId;
    private String url;
    private Images images;
    private String title;
    private String titleEnglish;
    private String synopsis;
    private String type;        // TV, Movie, OVA, etc.
    private Integer episodes;
    private String status;      // "Currently Airing", "Finished Airing", etc.
    private Double score;
    private Integer rank;
    private Integer popularity;
    private Integer year;
    private List<Genre> genres;
    private Trailer trailer;

    // ===== nested types =====
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Images {
        private ImageSet jpg;
        public ImageSet getJpg() { return jpg; }
        public void setJpg(ImageSet jpg) { this.jpg = jpg; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageSet {
        private String imageUrl;
        private String largeImageUrl;
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getLargeImageUrl() { return largeImageUrl; }
        public void setLargeImageUrl(String largeImageUrl) { this.largeImageUrl = largeImageUrl; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genre {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trailer {
        // Official YouTube trailer/PV only - legal, no pirated video content
        private String youtubeId;
        private String embedUrl;
        public String getYoutubeId() { return youtubeId; }
        public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }
        public String getEmbedUrl() { return embedUrl; }
        public void setEmbedUrl(String embedUrl) { this.embedUrl = embedUrl; }
    }

    // ===== convenience helper used by templates =====
    public String getPosterUrl() {
        if (images != null && images.getJpg() != null) {
            return images.getJpg().getLargeImageUrl() != null
                    ? images.getJpg().getLargeImageUrl()
                    : images.getJpg().getImageUrl();
        }
        // Inline SVG placeholder - walang external file na kailangan, kahit
        // offline gagana ang fallback na ito kapag walang poster sa API.
        return "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='300' height='450'><rect width='100%' height='100%' fill='%231E1B2E'/><text x='50%' y='50%' fill='%239C97B8' font-size='18' text-anchor='middle' dy='.3em'>No Image</text></svg>";
    }

    // ===== getters and setters =====
    public Integer getMalId() { return malId; }
    public void setMalId(Integer malId) { this.malId = malId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Images getImages() { return images; }
    public void setImages(Images images) { this.images = images; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleEnglish() { return titleEnglish; }
    public void setTitleEnglish(String titleEnglish) { this.titleEnglish = titleEnglish; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getEpisodes() { return episodes; }
    public void setEpisodes(Integer episodes) { this.episodes = episodes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Integer getPopularity() { return popularity; }
    public void setPopularity(Integer popularity) { this.popularity = popularity; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public Trailer getTrailer() { return trailer; }
    public void setTrailer(Trailer trailer) { this.trailer = trailer; }
}
