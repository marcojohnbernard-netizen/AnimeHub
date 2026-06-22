package com.animehub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Jikan bumabalot ng single anime sa: { "data": {...} } */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeDetailResponse {
    private AnimeDto data;

    public AnimeDto getData() { return data; }
    public void setData(AnimeDto data) { this.data = data; }
}
