package com.example.library.management.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Additional cache configuration can be added here (e.g., using Redis)
}
