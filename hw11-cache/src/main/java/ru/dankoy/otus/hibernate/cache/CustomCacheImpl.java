package ru.dankoy.otus.hibernate.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class CustomCacheImpl<K, V> implements CustomCache<K, V> {

    WeakHashMap<K, V> cache = new WeakHashMap<>();
    List<CustomCacheListener<K, V>> listeners = new ArrayList<>();

    @Override
    public String toString() {
        return "CustomCacheImpl{" +
                "weakHashMap=" + cache +
                '}';
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.get(key);
        cache.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notify(key, value, "get");
        return cache.get(key);
    }

    @Override
    public void addListener(CustomCacheListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CustomCacheListener<K, V> listener) {
        listeners.remove(listener);
    }

    public void notify(K key, V value, String action) {
        try {
            listeners.forEach(l -> l.notify(key, value, action));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
