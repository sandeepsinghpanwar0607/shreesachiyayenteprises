package com.SSE.Website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.SSE.Website.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

    	http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .sessionManagement(session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // PUBLIC APIs
                // =========================
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/verify-otp",
                    "/api/users/register"
                ).permitAll()

                // =========================
                // PUBLIC ROOT URL
                // =========================
                .requestMatchers("/").permitAll()

                // =========================
                // PUBLIC PRODUCT IMAGES
                // =========================
                .requestMatchers(
                    "/uploads/**"
                ).permitAll()

                // =========================
                // PUBLIC PRODUCT APIs
                // Customers can view products
                // without login
                // =========================
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/products",
                    "/api/products/**"
                ).permitAll()

                // =========================
                // EVERYTHING ELSE
                // JWT REQUIRED
                // =========================
                .anyRequest().authenticated()
            )

            // JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}