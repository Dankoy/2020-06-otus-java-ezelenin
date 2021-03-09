package ru.dankoy.otus.hibernate.cache;

public interface CustomCacheListener<K, V> {
    void notify(K key, V value, String action);
}
