package com.server.d2ackserver.domain.auth.repository.impl;


import com.server.d2ackserver.domain.auth.repository.RefreshTokenRepository;
import com.server.d2ackserver.global.security.jwt.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String email, String refreshToken) {
        redisTemplate.opsForValue().set("refreshToken:" + email, refreshToken, jwtProperties.getRefreshTokenExpiration(), TimeUnit.MILLISECONDS);  // email -> userId
    }

    @Override
    public Optional<String> findByEmail(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("refreshToken:" + email));  // email -> userId
    }

    @Override
    public void deleteByEmail(String email) {
        redisTemplate.delete("refreshToken:" + email);  // email -> userId
    }

    @Override
    public boolean existsByEmail(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("refreshToken:" + email));  // email -> userId
    }
}
