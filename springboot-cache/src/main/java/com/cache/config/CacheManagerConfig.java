package com.cache.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lovely
 * @version 1.0
 * on 2021/8/6
 */
@Configuration
public class CacheManagerConfig implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
    private volatile Set<String> cacheNames = Collections.emptySet();

    @Override
    @Nullable
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache != null) {
            return cache;
        }

        Cache missingCache = createRedisCache(name, new HashMap<>());
        if (missingCache != null) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = missingCache;
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        System.out.println("--------getCacheNames-------");
        return cacheNames;
    }

    protected SelfCache createRedisCache(String name, Map<String, Object> map) {
        return new SelfCache(name, map);
    }
}
