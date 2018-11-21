package com.jkojote.weblib.application.utils;

import com.jkojote.library.persistence.MapCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapCacheImpl<K, V> implements MapCache<K, V> {

    private final Map<K, V> map;

    private final int maxCapacity;

    private boolean disabled;

    public MapCacheImpl(int maxCapacity) {
        this.map = new ConcurrentHashMap<>();
        this.maxCapacity = maxCapacity;
    }

    public MapCacheImpl() {
        this(256);
    }

    @Override
    public boolean contains(K k) {
        if (disabled)
            return false;
        return map.containsKey(k);
    }

    @Override
    public boolean put(K k, V v) {
        if (disabled)
            return false;
        if (map.size() < maxCapacity)
            map.put(k, v);
        return true;
    }

    @Override
    public V get(K k) {
        if (disabled)
            return null;
        return map.get(k);
    }

    @Override
    public boolean remove(K k) {
        if (disabled)
            return false;
        return map.remove(k) == null;
    }

    @Override
    public synchronized void disable() {
        disabled = true;
    }

    @Override
    public synchronized void enable() {
        disabled = false;
    }

    @Override
    public void clean() {
        if (disabled)
            return;
        map.clear();
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public int maxCapacity() {
        return maxCapacity;
    }

    @Override
    public int size() {
        return map.size();
    }
}
