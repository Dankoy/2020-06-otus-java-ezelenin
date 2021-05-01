package reflection;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionHelper {

    /**
     * Инжектит в любой объект указанное поле
     *
     * @param clazz
     * @param object
     */
    public static void setAnyFieldInObject(Class<?> clazz, Object object, Object objectToInject,
            String fieldName) throws NoSuchFieldException, IllegalAccessException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, objectToInject);
    }

    /**
     * Заполняет поля объекта, переданные в Map, где ключ - имя поля, а значение - это значение поля.
     *
     * @param clazz
     * @param object
     * @param fieldsToInject
     */
    public static void setFieldsInObject(Class<?> clazz, Object object,
            Map<Object, Object> fieldsToInject) {

        fieldsToInject.forEach((k, v) -> {
            try {
                Field field = clazz.getDeclaredField(k.toString());
                field.setAccessible(true);
                field.set(object, v);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Получение значения поля объекта по имени поля
     *
     * @param clazz
     * @param object
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Class<?> clazz, Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

}
