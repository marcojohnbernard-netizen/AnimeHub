package com.animehub.service;

import com.animehub.dto.AnimeDetailResponse;
import com.animehub.dto.AnimeDto;
import com.animehub.dto.AnimeListResponse;
import com.animehub.exception.JikanApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Lahat ng pag-uusap natin sa Jikan API (https://docs.api.jikan.moe) ay
 * dito lang dadaan. Layunin: hiwalay ang "external API logic" sa
 * controllers, kaya kung magpalit man tayo ng data source balang araw,
 * dito lang ang babaguhin.
 *
 * Bawat method ay may try-catch dahil ang Jikan ay isang THIRD-PARTY
 * service - pwede itong mag-timeout, magbigay ng rate-limit error (429),
 * o mawala bigla. Hindi dapat masira ang buong site kapag nangyari ito.
 */
@Service
public class JikanApiService {

    private static final Logger log = LoggerFactory.getLogger(JikanApiService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public JikanApiService(RestTemplate restTemplate,
                            @Value("${jikan.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /** Mga anime na kasalumuhang umaairing season ("latest anime"). */
    @Cacheable(value = "latestAnime", key = "#page")
    public List<AnimeDto> getLatestAnime(int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/seasons/now")
                .queryParam("page", page)
                .queryParam("limit", 24)
                .toUriString();
        return fetchList(url, "latest anime");
    }

    /** Top-ranking anime base sa score/popularity sa MyAnimeList. */
    @Cacheable(value = "topAnime", key = "#page")
    public List<AnimeDto> getTopAnime(int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/top/anime")
                .queryParam("page", page)
                .queryParam("limit", 24)
                .toUriString();
        return fetchList(url, "top anime ranking");
    }

    /** Currently trending - same ranking pool but sorted by popularity (most-watched right now). */
    @Cacheable(value = "trendingAnime", key = "#page")
    public List<AnimeDto> getTrendingAnime(int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/top/anime")
                .queryParam("filter", "bypopularity")
                .queryParam("page", page)
                .queryParam("limit", 24)
                .toUriString();
        return fetchList(url, "trending anime");
    }

    /** Anime that have finished airing completely - good for binge-watch browsing. */
    @Cacheable(value = "completeAnime", key = "#page")
    public List<AnimeDto> getCompleteAnime(int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/anime")
                .queryParam("status", "complete")
                .queryParam("order_by", "popularity")
                .queryParam("sort", "asc")
                .queryParam("page", page)
                .queryParam("limit", 24)
                .toUriString();
        return fetchList(url, "complete anime");
    }

    /**
     * Browse by genre. Jikan identifies genres by numeric ID - see GENRES map
     * in GenreController for the common ones we expose in the UI.
     */
    @Cacheable(value = "genreAnime", key = "#genreId + '-' + #page")
    public List<AnimeDto> getAnimeByGenre(int genreId, int page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/anime")
                .queryParam("genres", genreId)
                .queryParam("order_by", "popularity")
                .queryParam("sort", "asc")
                .queryParam("page", page)
                .queryParam("limit", 24)
                .toUriString();
        return fetchList(url, "genre anime");
    }

    /** Maghanap ng anime base sa keyword. */
    @Cacheable(value = "animeSearch", key = "#query + '-' + #page")
    public List<AnimeDto> searchAnime(String query, int page) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/anime")
                .queryParam("q", query.trim())
                .queryParam("page", page)
                .queryParam("limit", 24)
                .queryParam("order_by", "popularity")
                .queryParam("sort", "asc")
                .toUriString();
        return fetchList(url, "search results para sa \"" + query + "\"");
    }

    /** Detailed information for a single anime (including the trailer). */
    @Cacheable(value = "animeDetail", key = "#malId")
    public AnimeDto getAnimeDetail(int malId) {
        String url = baseUrl + "/anime/" + malId + "/full";
        try {
            AnimeDetailResponse response = restTemplate.getForObject(url, AnimeDetailResponse.class);
            if (response == null || response.getData() == null) {
                throw new JikanApiException("No anime found for ID: " + malId);
            }
            return response.getData();
        } catch (HttpClientErrorException.NotFound e) {
            throw new JikanApiException("This anime could not be found.", e);
        } catch (ResourceAccessException e) {
            log.warn("Timeout/network error from Jikan API: {}", e.getMessage());
            throw new JikanApiException("The anime data service took too long to respond.", e);
        } catch (Exception e) {
            log.error("Unexpected error during Jikan API call", e);
            throw new JikanApiException("Could not retrieve anime details right now.", e);
        }
    }

    // ===== shared helper para sa lahat ng list-returning endpoints =====
    private List<AnimeDto> fetchList(String url, String contextLabel) {
        try {
            AnimeListResponse response = restTemplate.getForObject(url, AnimeListResponse.class);
            return response != null ? response.getData() : Collections.emptyList();
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Rate-limited by Jikan API while fetching {}", contextLabel);
            // Hindi natin itinatapon ang error dito - mas mabuting mag-return ng
            // walang lamang listahan kaysa biglain ang user ng error page
            // dahil lang sa rate limit ng third-party API.
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            log.warn("Timeout habang kinukuha ang {}: {}", contextLabel, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Hindi inaasahang error habang kinukuha ang {}", contextLabel, e);
            return Collections.emptyList();
        }
    }
}
