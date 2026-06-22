package com.animehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // turns on @Cacheable so Jikan API calls aren't repeated unnecessarily
public class AnimehubApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimehubApplication.class, args);
    }
}
