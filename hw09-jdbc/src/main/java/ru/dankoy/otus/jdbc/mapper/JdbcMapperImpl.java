package ru.dankoy.otus.jdbc.mapper;

import ru.dankoy.otus.jdbc.DbExecutor;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

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
    public void insert(Object objectData) {

        String className = entityClassMetaData.getName();

        try {
            List<Object> fieldWithoutIdValues = getFieldsWithoutIdValues(objectData);

            var obj = Class.forName(className).cast(objectData);
            System.out.println(obj.getClass());
            dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                    fieldWithoutIdValues);
        } catch (ClassNotFoundException | SQLException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void update(Object objectData) {

    }

    @Override
    public void insertOrUpdate(Object objectData) {

    }

    @Override
    public Object findById(Object id, Class clazz) {

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

}
