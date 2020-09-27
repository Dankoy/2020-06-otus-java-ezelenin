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
import java.util.Collection;
import java.util.List;

public class Serializer {

    private JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    private JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

    public String serializeObject(Object object) throws IOException {

        buildJson(object);

        return getJsonStringFromJsonObjectBuilder(objectBuilder);
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
            Class<?> fieldType = getFieldType(objectField);
            boolean isFieldPrimitive = isFieldPrimitive(fieldType);
            if (isFieldPrimitive) {

                switchCasePrimitiveTypeToObjectBuilder(fieldType, object, objectField);

            } else {

                switchCaseWrapperTypeToObjectBuilder(fieldType, object, objectField);
            }

            if (checkIfInterfaceIsPresent(getInterfaces(fieldType), Collection.class)) {

                List<?> list = null;
                Object fieldValue = getFieldValue(object, objectField);
                if (fieldValue != null) {
                    list = new ArrayList<>((Collection<?>) getFieldValue(object, objectField));
                } else {
                    list = new ArrayList<>();
                }
                for (Object item : list) {
                    switchCaseAnyTypeToArrayBuilder(item);
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
     * Получение типа поля
     *
     * @param object
     * @return
     */
    private Class<?> getFieldType(Object object) {

        return object.getClass();

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
     * Проверка, если тип примитивный или нет.
     * По лекции 17 - считаем String тоже примитивным типом
     *
     * @param fieldTypeClass
     * @return
     */
    private boolean isFieldPrimitive(Class<?> fieldTypeClass) {

        if (fieldTypeClass.getSimpleName().equals("String"))
            return true;

        return fieldTypeClass.isPrimitive();
    }

    /**
     * Получение списка интерфейсов, которые реализовывает класс типа поля
     *
     * @param field
     * @return
     */
    private Class<?>[] getInterfaces(Class<?> field) {
        return field.getInterfaces();
    }

    /**
     * Проверяется, если у типа толя присутствует выбранный интерфейс.
     * Например, проверяется интерфейс Collection, что бы определить, что поле имеет тип List.
     *
     * @param interfaces
     * @param interfazeName
     * @return
     */
    private boolean checkIfInterfaceIsPresent(Class<?>[] interfaces, Class<?> interfazeName) {
        boolean result = false;
        for (Class interfaze : interfaces) {
            if (interfaze.equals(interfazeName)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * Конвертация значения переменной объекта и приведение ее к нужному примитивному типу.
     *
     * @param fieldType
     * @param object
     * @param objectField
     */
    private void switchCasePrimitiveTypeToObjectBuilder(Class<?> fieldType, Object object, Field objectField) {

        switch (fieldType.getSimpleName()) {
            case ("int"): {
                objectBuilder.add(getFieldName(objectField),
                        (int) getFieldValue(object, objectField));
                break;
            }
            case ("boolean"): {
                objectBuilder.add(getFieldName(objectField),
                        (boolean) getFieldValue(object, objectField));
                break;
            }
            case ("String"): {
                objectBuilder.add(getFieldName(objectField),
                        (String) getFieldValue(object, objectField));
                break;
            }
            case ("long"): {
                objectBuilder.add(getFieldName(objectField),
                        (long) getFieldValue(object, objectField));
                break;
            }
            case ("double"): {
                objectBuilder.add(getFieldName(objectField),
                        (double) getFieldValue(object, objectField));
                break;
            }
            case ("float"): {
                objectBuilder.add(getFieldName(objectField),
                        (float) getFieldValue(object, objectField));
                break;
            }
            case ("byte"): {
                objectBuilder.add(getFieldName(objectField),
                        (byte) getFieldValue(object, objectField));
                break;
            }
            case ("char"): {
                objectBuilder.add(getFieldName(objectField),
                        (char) getFieldValue(object, objectField));
                break;
            }
            case ("short"): {
                objectBuilder.add(getFieldName(objectField),
                        (short) getFieldValue(object, objectField));
                break;
            }
        }
    }

    /**
     * Конвертация значения переменной объекта и приведение ее к нужному типу обертки.
     *
     * @param fieldType
     * @param object
     * @param objectField
     */
    private void switchCaseWrapperTypeToObjectBuilder(Class<?> fieldType, Object object, Field objectField) {

        switch (fieldType.getSimpleName()) {
            case ("Integer"): {
                objectBuilder.add(getFieldName(objectField),
                        (Integer) getFieldValue(object, objectField));
                break;
            }
            case ("Boolean"): {
                objectBuilder.add(getFieldName(objectField),
                        (Boolean) getFieldValue(object, objectField));
                break;
            }
            case ("String"): {
                objectBuilder.add(getFieldName(objectField),
                        (String) getFieldValue(object, objectField));
                break;
            }
            case ("Long"): {
                objectBuilder.add(getFieldName(objectField),
                        (Long) getFieldValue(object, objectField));
                break;
            }
            case ("Double"): {
                objectBuilder.add(getFieldName(objectField),
                        (Double) getFieldValue(object, objectField));
                break;
            }
            case ("Float"): {
                objectBuilder.add(getFieldName(objectField),
                        (Float) getFieldValue(object, objectField));
                break;
            }
            case ("Byte"): {
                objectBuilder.add(getFieldName(objectField),
                        (Byte) getFieldValue(object, objectField));
                break;
            }
            case ("Char"): {
                objectBuilder.add(getFieldName(objectField),
                        (Character) getFieldValue(object, objectField));
                break;
            }
            case ("Short"): {
                objectBuilder.add(getFieldName(objectField),
                        (Short) getFieldValue(object, objectField));
                break;
            }
        }

    }

    /**
     * Проверка типа объекта в листе и приведение его к нужному типу
     *
     * @param value
     */
    private void switchCaseAnyTypeToArrayBuilder(Object value) {

        if (!value.getClass().isPrimitive()) {

            switch (value.getClass().getSimpleName()) {
                case ("Integer"): {
                    arrayBuilder.add((Integer) value);
                    break;
                }
                case ("Boolean"): {
                    arrayBuilder.add((Boolean) value);
                    break;
                }
                case ("String"): {
                    arrayBuilder.add((String) value);
                    ;
                    break;
                }
                case ("Long"): {
                    arrayBuilder.add((Long) value);
                    break;
                }
                case ("Double"): {
                    arrayBuilder.add((Double) value);
                    break;
                }
                case ("Float"): {
                    arrayBuilder.add((Float) value);
                    break;
                }
                case ("Byte"): {
                    arrayBuilder.add((Byte) value);
                    break;
                }
                case ("Char"): {
                    arrayBuilder.add((Character) value);
                    break;
                }
                case ("Short"): {
                    arrayBuilder.add((Short) value);
                    break;
                }
            }

        } else {

            switch (value.getClass().getSimpleName()) {
                case ("int"): {
                    arrayBuilder.add((int) value);
                    break;
                }
                case ("boolean"): {
                    arrayBuilder.add((boolean) value);
                    break;
                }
                case ("long"): {
                    arrayBuilder.add((long) value);
                    break;
                }
                case ("double"): {
                    arrayBuilder.add((double) value);
                    break;
                }
                case ("float"): {
                    arrayBuilder.add((float) value);
                    break;
                }
                case ("byte"): {
                    arrayBuilder.add((byte) value);
                    break;
                }
                case ("char"): {
                    arrayBuilder.add((char) value);
                    break;
                }
                case ("short"): {
                    arrayBuilder.add((short) value);
                    break;
                }
            }

        }

    }

}
