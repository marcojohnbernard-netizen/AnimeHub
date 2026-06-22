package com.animehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * BCrypt = one-way hashing. Hindi natin ito-decrypt ang password pabalik -
     * inihahambing lang natin ang hash ng input vs ang naka-store na hash.
     * Kailanman, huwag i-store ang plain text password sa database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Pwedeng tingnan kahit walang account - browsing/search/ranking ay dapat public
                .requestMatchers(
                        "/", "/search", "/ranking", "/anime/**",
                        "/register", "/login", "/css/**", "/js/**", "/h2-console/**"
                ).permitAll()
                // Watchlist ay dapat may account muna
                .requestMatchers("/watchlist/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // H2 console gamit ang frames - kailangan i-relax ang frame options
            // PARA LANG ITO SA DEVELOPMENT. Tanggalin ang h2-console sa production.
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .csrf(csrf -> csrf
                // H2 console has its own form handling that conflicts with CSRF; everything else keeps CSRF protection
                .ignoringRequestMatchers("/h2-console/**")
            );

        return http.build();
    }
}
