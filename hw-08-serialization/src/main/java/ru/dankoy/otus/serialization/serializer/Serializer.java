package ru.dankoy.otus.serialization.serializer;

import javax.json.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Serializer {

    private JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    public String serializeObject(Object object) throws IOException {

        if (object == null) {
            return "null";
        } else {
            return buildJson(object);
        }

    }

    //TODO Не реализована рекурсия

    /**
     * Строительство json.
     *
     * @param object
     * @throws IOException
     */
    private String buildJson(Object object) throws IOException {

        if (isPrimitive(object)) {
            return String.valueOf(object);
        } else if (isCharOrString(object)) {
            return "\"" + object + "\"";
        } else if (object.getClass().isArray()) {
            return String.valueOf(serializeArray(object));
        } else if (object instanceof Collection) {
            return String.valueOf(serializeCollection(object));
        }

        return buildObject(object);

    }


    /**
     * Проверка на примитивный тип
     *
     * @param obj
     * @return
     */
    private boolean isPrimitive(Object obj) {
        if (obj instanceof Boolean) return true;
        if (obj instanceof Byte) return true;
        if (obj instanceof Integer) return true;
        if (obj instanceof Short) return true;
        if (obj instanceof Long) return true;
        if (obj instanceof Float) return true;
        if (obj instanceof Double) return true;
        return false;
    }

    /**
     * Проверка на String или Char
     *
     * @param obj
     * @return
     */
    private boolean isCharOrString(Object obj) {

        if (obj instanceof String) return true;
        else return obj instanceof Character;
    }


    /**
     * Билд json объекта
     *
     * @param object
     * @return
     * @throws IOException
     */
    private String buildObject(Object object) throws IOException {

        Class clazz = getClassFromObject(object);
        Field[] objectFields = getObjectFields(clazz);

        for (Field objectField : objectFields) {

            Object fieldValue = getFieldValue(object, objectField);

            if (fieldValue != null) {

                // парсинг примитива
                if (isPrimitive(fieldValue) || isCharOrString(fieldValue)) {
                    checkPrimitiveFieldTypeAndGetCorrectValue(object, objectField);
                }

                // парсинг коллекции
                if (fieldValue instanceof Collection) {
                    JsonValue array = serializeCollection(fieldValue);
                    objectBuilder.add(getFieldName(objectField), array);

                }

                // Парсинг массива
                if (fieldValue.getClass().isArray()) {
                    JsonArray array = serializeArray(fieldValue);

                    objectBuilder.add(getFieldName(objectField), array);
                }
            }

        }

        return getJsonStringFromJsonObjectBuilder(objectBuilder);

    }

    /**
     * Сериализация массива
     *
     * @param obj
     * @return
     */
    private JsonArray serializeArray(Object obj) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int i = 0; i < Array.getLength(obj); i++) {
            switchSerializeArray(Array.get(obj, i), arrayBuilder);
        }

        return arrayBuilder.build();

    }

    /**
     * Сериализация коллекции
     *
     * @param collection
     * @return
     */
    private JsonArray serializeCollection(Object collection) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        List<?> list = new ArrayList<>((Collection<?>) collection);
        for (Object item : list) {
            switchSerializeArray(item, arrayBuilder);
        }

        return arrayBuilder.build();

    }


    /**
     * Проверка типа объекта в листе и приведение его к нужному типу
     *
     * @param value
     */
    private void switchSerializeArray(Object value, JsonArrayBuilder arrayBuilder) {

        if (value instanceof Integer) {
            arrayBuilder.add((int) value);
        } else if (value instanceof Boolean) {
            arrayBuilder.add((boolean) value);
        } else if (value instanceof Byte) {
            arrayBuilder.add((byte) value);
        } else if (value instanceof Short) {
            arrayBuilder.add((short) value);
        } else if (value instanceof Long) {
            arrayBuilder.add((long) value);
        } else if (value instanceof Float) {
            arrayBuilder.add((float) value);
        } else if (value instanceof Double) {
            arrayBuilder.add((double) value);
        } else if (value instanceof String) {
            arrayBuilder.add((String) value);
        } else if (value instanceof Character) {
            arrayBuilder.add((char) value);
        }

    }

    /**
     * Получение финальной строки в формате json для объектов
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

    /**
     * Получение имени поля
     *
     * @param field
     * @return
     */
    private String getFieldName(Field field) {
        return field.getName();
    }

    /**
     * Конвертация значения переменной объекта и приведение ее к нужному примитивному типу.
     *
     * @param object
     * @param objectField
     */
    private void checkPrimitiveFieldTypeAndGetCorrectValue(Object object, Field objectField) {

        String fieldName = getFieldName(objectField);
        Object fieldValue = getFieldValue(object, objectField);

        if (fieldValue instanceof Integer) {
            objectBuilder.add(fieldName, (int) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            objectBuilder.add(fieldName, (boolean) fieldValue);
        } else if (fieldValue instanceof Byte) {
            objectBuilder.add(fieldName, (byte) fieldValue);
        } else if (fieldValue instanceof Short) {
            objectBuilder.add(fieldName, (short) fieldValue);
        } else if (fieldValue instanceof Long) {
            objectBuilder.add(fieldName, (long) fieldValue);
        } else if (fieldValue instanceof Float) {
            objectBuilder.add(fieldName, (float) fieldValue);
        } else if (fieldValue instanceof Double) {
            objectBuilder.add(fieldName, (double) fieldValue);
        } else if (fieldValue instanceof String) {
            objectBuilder.add(fieldName, (String) fieldValue);
        } else if (fieldValue instanceof Character) {
            objectBuilder.add(fieldName, (char) fieldValue);
        }
    }

}
