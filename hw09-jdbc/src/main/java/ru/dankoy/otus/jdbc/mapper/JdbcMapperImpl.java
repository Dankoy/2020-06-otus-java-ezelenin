package ru.dankoy.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.jdbc.DbExecutor;
import ru.dankoy.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void insert(Object objectData) {

        try {
            List<Object> fieldWithoutIdValues = getFieldsWithoutIdValues(objectData);

            dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                    fieldWithoutIdValues);
        } catch (SQLException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void update(Object objectData) {

    }

    @Override
    public void insertOrUpdate(Object objectData) {

    }

    // TODO: Сделать универсальный хэндлер
    @Override
    public Object findById(Object id, Class clazz) {

        try {

            System.out.println(entitySQLMetaData.getSelectByIdSql());
            dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                System.out.println(rs.toString());
//                                return new User(rs.getLong("id"), rs.getString("name"));
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

}
