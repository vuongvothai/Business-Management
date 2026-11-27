package com.businessmanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/master/**", "/api/weigh-tickets/**", "/api/purchase/**",
                "/api/consignment/**", "/api/sales/**", "/api/inventory/**",
                "/api/payment/**", "/api/expense/**", "/api/loan/**",
                "/api/report/**").permitAll()
                            .anyRequest().permitAll()
            );
        return http.build();
    }
}