package com.example.authapi.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;

    public JwtService(@Value("${app.jwt-secret}") String secret) {
        byte[] bytes = sha256(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    private static byte[] sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("JWT key derivation failed", e);
        }
    }

    public String generateToken(String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L*60*60*12))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String validateAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
