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

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("select ")
                .append(makeFieldNamesStringWithComma(entityClassMetaData.getAllFields()))
                .append(" from ")
                .append(getTableName().toLowerCase());

        return stringBuilder.toString();
    }

    @Override
    public String getSelectByIdSql() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("select ")
                .append(makeFieldNamesStringWithComma(entityClassMetaData.getAllFields()))
                .append(" from ")
                .append(getTableName().toLowerCase())
                .append(" where ")
                .append(entityClassMetaData.getIdField().getName())
                .append(" = ?");

        return stringBuilder.toString();
    }

    @Override
    public String getInsertSql() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("insert into ")
                .append(getTableName().toLowerCase())
                .append("(")
                .append(makeFieldNamesStringWithComma(entityClassMetaData.getFieldsWithoutId()))
                .append(") values ")
                .append(makeQuestionMarks(entityClassMetaData.getFieldsWithoutId()));

        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("update ")
                .append(getTableName().toLowerCase())
                .append(" set ")
                .append(makeSqlForUpdate(this.entityClassMetaData.getFieldsWithoutId()))
                .append(" where ")
                .append(this.entityClassMetaData.getIdField().getName())
                .append("= ?");

        return stringBuilder.toString();
    }

    private String getTableName() {

        String tableName = entityClassMetaData.getName();
        return tableName.substring(tableName.lastIndexOf(".") + 1);

    }

    /**
     * Получение строки с именами полей объекта через запятую.
     * При передачи списка всех полей - генерирует строку для метода getSelectAllSql()
     * При передачи списка полей без аннотации id - генерирует строку для метода getInsertSql()
     *
     * @param fields
     * @return
     */
    private String makeFieldNamesStringWithComma(List<Field> fields) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Field field : fields) {
            String fieldName = field.getName();
            stringBuilder.append(fieldName);

            if (fields.indexOf(field) != fields.size() - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    /**
     * Получение строки с вопросиками в завсимости от количества полей без аннотации id
     *
     * @param fields
     * @return
     */
    private String makeQuestionMarks(List<Field> fields) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");

        for (Field field : fields) {
            stringBuilder.append("?");

            if (fields.indexOf(field) != fields.size() - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(")");

        return stringBuilder.toString();

    }

    private String makeSqlForUpdate(List<Field> fields) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Field field : fields) {
            stringBuilder
                    .append(field.getName())
                    .append("=?");

            if (fields.indexOf(field) != fields.size() - 1)
                stringBuilder.append(", ");
        }

        return stringBuilder.toString();
    }

}
