package ru.dankoy.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {

        return createSimpleSqlSelectAllFromTable().append(";").toString();
    }

    @Override
    public String getSelectByIdSql() {

        StringBuilder stringBuilder = createSimpleSqlSelectAllFromTable();

        stringBuilder.append(" where ").append(entityClassMetaData.getIdField().getName()).append(" == (?);");

        return stringBuilder.toString();
    }

    @Override
    public String getInsertSql() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into (").append(entityClassMetaData.getName().toLowerCase())
                .append(") values (?);");

        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

    private StringBuilder createSimpleSqlSelectAllFromTable() {

        List<Field> fields = entityClassMetaData.getAllFields();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select ");

        for (Field field : fields) {

            String fieldName = field.getName();
            stringBuilder.append(fieldName);

            if (fields.indexOf(field) != fields.size() - 1)
                stringBuilder.append(", ");
            else
                stringBuilder.append(" ");
        }

        String tableName = entityClassMetaData.getName();
        stringBuilder.append("from ").append(tableName.toLowerCase());

        return stringBuilder;
    }

}
