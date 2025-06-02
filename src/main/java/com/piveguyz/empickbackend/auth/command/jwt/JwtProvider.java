package com.piveguyz.empickbackend.auth.command.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;
    private final long accessTokenValidity = 1000 * 60 * 60; // 1시간
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 1주일

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(int memberId, Integer employeeNumber) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .claim("employeeNumber", employeeNumber)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(int memberId, Integer employeeNumber) {
        return Jwts.builder()
                .setSubject(String.valueOf(employeeNumber))
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }
}
