package com.example.teamtwelvebackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${cors.allow-origin-patterns}")
    private List<String> originPatterns;

    final JwtDecoder jwtDecoder;

    public WebSecurityConfig(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtDecoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers(headers -> headers
                        // allow same origin to frame our site to support iframe SockJS
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()
                        )
                )
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeHttpRequests()
                    .requestMatchers("/", "/ws", "/ws/**", "/qr/**", "/docs/**", "/activities",
                            "/csrf",
                            "/app/**", "/user/**", "/topic/**",
                            "/activity/speedgame/**", "/activity/moodcheckin/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .oauth2ResourceServer()
                    .jwt();

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(originPatterns.toArray(String[]::new));
            }
        };
    }
}
