package ru.dankoy.otus.serialization.serializer;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Serializer {

    private JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    private JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

    public String serializeObject(Object object) throws IOException {

        buildJson(object);
        System.out.println(getJsonStringFromJsonObjectBuilder(objectBuilder));

        return null;
    }

    //TODO Не реализована рекурсия и поиск и проверка массивов
    /**
     * Строительство json.
     *
     * @param object
     * @throws IOException
     */
    private void buildJson(Object object) {

        Class clazz = getClassFromObject(object);
        Field[] objectFields = getObjectFields(clazz);

        for (Field objectField : objectFields) {
            System.out.println(getFieldType(objectField));
            Class type = getFieldType(objectField);
            System.out.println(Arrays.toString(type.getInterfaces()));
            System.out.println(checkIfInterfaceIsPresent(getInterfaces(type), Collection.class));
        }
//
//        for (Field field : objectFields) {
//            System.out.println(getFieldValue(object, field));
//        }


        for (Field objectField : objectFields) {
            Class fieldType = getFieldType(objectField);
            boolean isFieldPrimitive = isFieldPrimitive(fieldType);
            if (isFieldPrimitive) {
                objectBuilder.add(getFieldName(objectField), getFieldValue(object, objectField).toString());
            } else if (checkIfInterfaceIsPresent(getInterfaces(fieldType), Collection.class)) {
                List<?> list = new ArrayList<>((Collection<?>)getFieldValue(object, objectField));
                for (Object item : list) {
                    arrayBuilder.add(item.toString());
                }
                objectBuilder.add(getFieldName(objectField), arrayBuilder);
            }
        }

    }

    /**
     * Получение финальной строки в формате json
     *
     * @param jsonObjectBuilder
     * @return
     * @throws IOException
     */
    private String getJsonStringFromJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder) throws IOException {

        JsonObject jsonObject = jsonObjectBuilder.build();
        String jsonString;
        try (Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(jsonObject);
            jsonString = writer.toString();
        }

        return jsonString;

    }

    /**
     * Получение класса объекта
     *
     * @param object
     * @return
     */
    private Class getClassFromObject(Object object) {
        return object.getClass();
    }

    /**
     * Получение массива полей класса.
     * <p>
     * Сюда можно добавить проверку на modifier transient.
     *
     * @param clazz
     * @return
     */
    private Field[] getObjectFields(Class<Type> clazz) {

        return clazz.getDeclaredFields();

    }

    /**
     * Получение типа поля
     *
     * @param field
     * @return
     */
    private Class<?> getFieldType(Field field) {

        return field.getType();

    }

    /**
     * Получение значения поля у объекта
     *
     * @param object
     * @param field
     * @return
     */
    private Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getFieldName(Field field) {
        return field.getName();
    }

    private boolean isFieldPrimitive(Class<?> fieldTypeClass) {
        return fieldTypeClass.isPrimitive();
    }

    private Class<?>[] getInterfaces(Class<?> field) {
        return field.getInterfaces();
    }

    private boolean checkIfInterfaceIsPresent(Class<?>[] interfaces, Class<?> interfazeName) {
        boolean result = false;
        for (Class interfaze : interfaces) {
            System.out.println(interfaze.getSimpleName());
            if (interfaze.equals(interfazeName)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }


}
