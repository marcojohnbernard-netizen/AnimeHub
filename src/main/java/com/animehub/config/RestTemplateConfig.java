package com.animehub.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    /**
     * Timeouts dito ay importante: kung tumagal ang Jikan API, hindi sasabit
     * (hang) ang buong request thread - mag-timeout lang at babalik tayo sa
     * fallback (tingnan ang JikanApiService) imbes na ma-lag ang site.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
.setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
