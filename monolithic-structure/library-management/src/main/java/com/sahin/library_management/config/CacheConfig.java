package com.sahin.library_management.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("libraryCards"),
                new ConcurrentMapCache("books"),
                new ConcurrentMapCache("bookItems"),
                new ConcurrentMapCache("authors"),
                new ConcurrentMapCache("categories"),
                new ConcurrentMapCache("accounts"),
                new ConcurrentMapCache("racks")
        ));
        return cacheManager;
    }

    @Bean
    public UserCache userCache() {
        return new SpringCacheBasedUserCache(cacheManager().getCache("libraryCards"));
    }
}
