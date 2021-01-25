package ru.dankoy.otus.warmvc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCacheListenerImpl<K, V> implements CustomCacheListener<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(CustomCacheListenerImpl.class);

    @Override
    public void notify(K key, V value, String action) {
        logger.info("key:{}, value:{}, action: {}", key, value, action);
    }
}
