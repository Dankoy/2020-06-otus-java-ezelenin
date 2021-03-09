package ru.dankoy.otus.jetty.cache;

public interface CustomCacheListener<K, V> {
    void notify(K key, V value, String action);
}
