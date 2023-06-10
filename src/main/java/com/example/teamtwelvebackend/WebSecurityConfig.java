package com.example.teamtwelvebackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
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
                    .requestMatchers("/", "/ws", "/ws/**", "/qr/**", "/docs/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .oauth2ResourceServer()
                    .jwt();

        return http.build();
    }

}
