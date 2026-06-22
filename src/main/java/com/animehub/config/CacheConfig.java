package com.animehub.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Pinapanatili nito ang resulta ng mga Jikan API calls sa memory ng ilang
 * minuto. Dalawang benepisyo:
 *   1. Performance  - hindi na hihintayin ulit ng user ang external API
 *      kapag pareho lang ang request (hal. parehong search term).
 *   2. Reliability   - hindi tayo babasagin ng Jikan's rate limit
 *      (~60 requests/minute) kapag dumaming users.
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                "latestAnime", "topAnime", "animeSearch", "animeDetail"
        );
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500));
        return manager;
    }
}
