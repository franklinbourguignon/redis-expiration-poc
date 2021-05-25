package com.expiration.redisexpirationpoc.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void createKeyWithExpirationTime(String key, String value, Integer ttl) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    public boolean keyExists(String key) {
        return redisTemplate.hasKey(key);
    }
}
