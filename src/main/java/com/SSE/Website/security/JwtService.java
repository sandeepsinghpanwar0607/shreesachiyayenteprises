package com.SSE.Website.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.SSE.Website.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY =
            "MyIndustrialSensorPlatformSecretKeyForJWT2026VerySecureKey";

    private final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour

    public SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + EXPIRATION_TIME
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }
}