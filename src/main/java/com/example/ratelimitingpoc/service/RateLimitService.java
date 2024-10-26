package com.example.ratelimitingpoc.service;

import com.example.ratelimitingpoc.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    @Autowired
    RateLimitConfig rateLimitConfig;

    public boolean tryConsume(String id) {
        Bucket bucket = rateLimitConfig.resolveBucket(id);
        return bucket.tryConsume(1);
    }


}

