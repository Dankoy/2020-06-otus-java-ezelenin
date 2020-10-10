package ru.dankoy.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jdbc.DbExecutor;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final DbExecutor<T> dbExecutor;
    private final SessionManagerJdbc sessionManager;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public JdbcMapperImpl(DbExecutor<T> dbExecutor, EntityClassMetaData<T> entityClassMetaData,
            SessionManagerJdbc sessionManager) {
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Override
    public long insert(Object objectData) {

        try {
            List<Object> fieldWithoutIdValues = getFieldsWithoutIdValues(objectData);

            return dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                    fieldWithoutIdValues);
        } catch (SQLException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public void update(Object objectData) {

    }

    @Override
    public void insertOrUpdate(Object objectData) {

    }

    @Override
    public Optional<T> findById(Object id, Class clazz) {

        try {

            return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return getResult(rs);
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    /**
     * Получает список values каждого поля которое не помечено аннотацией Id
     *
     * @param objectData
     * @return
     * @throws IllegalAccessException
     */
    private List<Object> getFieldsWithoutIdValues(Object objectData) throws IllegalAccessException {

        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> result = new ArrayList<>();

        for (Field field : fieldsWithoutId) {
            field.setAccessible(true);
            result.add(field.get(objectData));
        }
        return result;
    }


    /**
     * Парсинг рещультата полученного из бд
     *
     * @param rs
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private T getResult(ResultSet rs) {

        Constructor<T> constructor = entityClassMetaData.getConstructor();
        List<Field> fields = entityClassMetaData.getAllFields();

        T object = null;

        try {
            object = constructor.newInstance();

            T finalObject = object;
            fields.forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.set(finalObject, rs.getObject(field.getName()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            logger.info("Object: {}", object);
            return object;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;

    }

}
