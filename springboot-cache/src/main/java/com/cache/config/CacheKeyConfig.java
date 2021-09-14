package com.cache.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 自定义key生成器
 * @author lovely
 * on 2020/2/9
 */
@Configuration
public class CacheKeyConfig {

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> method.getName() + Arrays.asList(params);
    }


    // @Bean("cacheManager")
    // public CacheManager cacheManager() {
    //     // ConcurrentHashMap 缓存
    //     ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
    //
    //     // RedisCacheManager.builder()
    //     new RedisCacheManager();
    // }
}
