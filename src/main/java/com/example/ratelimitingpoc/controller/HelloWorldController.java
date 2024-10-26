package com.example.ratelimitingpoc.controller;

import com.example.ratelimitingpoc.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HelloWorldController {

    @Autowired
    RateLimitConfig rateLimitConfig;

    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello world";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getInfo(@PathVariable("id") String id)
    {
        Bucket bucket = rateLimitConfig.resolveBucket(id);
        if(bucket.tryConsume(1))
        {
            return ResponseEntity.status(200).body("Success for user " + id);
        }else {
            return ResponseEntity.status(429).body("Rate limit exceeded for user " + id);
        }

    }
}