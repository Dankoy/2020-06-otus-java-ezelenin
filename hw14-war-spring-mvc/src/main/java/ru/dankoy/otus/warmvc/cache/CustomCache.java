package ru.dankoy.otus.warmvc.cache;

public interface CustomCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(CustomCacheListener<K, V> listener);

    void removeListener(CustomCacheListener<K, V> listener);

}
