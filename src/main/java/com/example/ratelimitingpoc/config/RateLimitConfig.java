package com.example.ratelimitingpoc.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RateLimitConfig {
    //autowiring dependencies

    @Autowired
    public ProxyManager buckets;

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);
        return buckets.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        Refill refill = Refill.intervally(2, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(2, refill);

        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}
