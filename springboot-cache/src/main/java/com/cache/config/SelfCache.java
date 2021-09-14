package com.cache.config;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author lovely
 * @version 1.0
 * on 2021/8/6
 */
public class SelfCache extends AbstractValueAdaptingCache {

    private final Map<String, Object> map;
    private final String name;

    public SelfCache(String name, Map<String, Object> map) {
        super(false);
        this.name = name;
        this.map = map;
    }

    @Override
    protected Object lookup(Object key) {
        return map.get(key);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return map;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        Object o = map.get(key);
        if (o != null) {
            return (T) map.get(key);
        }
        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        map.put((String) key, value);
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {
        map.clear();
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }
}
