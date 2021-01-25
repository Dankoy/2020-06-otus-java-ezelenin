package ru.dankoy.otus.warmvc.cache;

public interface CustomCacheListener<K, V> {
    void notify(K key, V value, String action);
}
