package com.piveguyz.empickbackend.auth.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    public void saveRefreshToken(int memberId, String refreshToken, long expiryMillis) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expiryMillis));
    }

    public Optional<String> getRefreshToken(int memberId) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    public void deleteRefreshToken(int memberId) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        redisTemplate.delete(key);
    }

    public boolean validateRefreshToken(int memberId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(refreshToken);
    }
}
