package com.animehub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

/** Jikan bumabalot ng listahan ng anime sa: { "data": [...], "pagination": {...} } */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeListResponse {

    private List<AnimeDto> data = Collections.emptyList();
    private Pagination pagination;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagination {
        private Integer lastVisiblePage;
        private Boolean hasNextPage;
        private Integer currentPage;

        public Integer getLastVisiblePage() { return lastVisiblePage; }
        public void setLastVisiblePage(Integer lastVisiblePage) { this.lastVisiblePage = lastVisiblePage; }
        public Boolean getHasNextPage() { return hasNextPage; }
        public void setHasNextPage(Boolean hasNextPage) { this.hasNextPage = hasNextPage; }
        public Integer getCurrentPage() { return currentPage; }
        public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    }

    public List<AnimeDto> getData() { return data; }
    public void setData(List<AnimeDto> data) { this.data = data; }
    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }
}
