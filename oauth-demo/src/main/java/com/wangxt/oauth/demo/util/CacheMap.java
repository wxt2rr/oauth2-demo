package com.wangxt.oauth.demo.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheMap<K, V> {
    private final Cache<K, V> cache;

    public CacheMap(long timeout, TimeUnit unit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(timeout, unit)
                .build();
    }

    public V get(K key) {
        return cache.getIfPresent(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void remove(K key) {
        cache.invalidate(key);
    }

    public void removeAll() {
        cache.invalidateAll();
    }

    public long size() {
        return cache.size();
    }
}
