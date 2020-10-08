package ru.dankoy.otus.jdbc.mapper;

import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 *
 * @param <T>
 */
public interface JdbcMapper<T> {
    long insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);

    Optional<T> findById(Object id, Class<T> clazz);
}
