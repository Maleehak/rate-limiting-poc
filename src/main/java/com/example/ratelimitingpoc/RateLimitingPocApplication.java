package com.example.ratelimitingpoc;

import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.Iterator;

@SpringBootApplication
public class RateLimitingPocApplication {

	public static void main(String[] args) {
		//list all the caching provider
		Iterator<CachingProvider> iterator = Caching.getCachingProviders(Caching.getDefaultClassLoader()).iterator();
		while(iterator.hasNext()) {
            try (CachingProvider provider = iterator.next()) {
                if (!(provider instanceof EhcacheCachingProvider)) {
                    iterator.remove();
                }
            }
        }
		SpringApplication.run(RateLimitingPocApplication.class, args);
	}

}
