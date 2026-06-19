package com.attendance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "attendancemanagementsystemjwtsecretkeyattendance2026";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 Hours

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(
            String email,
            String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME))
                .signWith(
                        key,
                        SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(
            String token) {

        return extractClaims(token)
                .getSubject();
    }

    public String extractRole(
            String token) {

        return extractClaims(token)
                .get("role", String.class);
    }

    public boolean validateToken(
            String token) {

        try {

            extractClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private Claims extractClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}