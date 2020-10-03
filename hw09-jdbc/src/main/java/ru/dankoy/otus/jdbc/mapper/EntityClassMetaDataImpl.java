package ru.dankoy.otus.jdbc.mapper;

import ru.dankoy.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Class<?> clazz;
    private String className;
    private List<Field> fieldsWithoutId;
    private List<Field> allFields;
    private Field fieldWithId;
    private Constructor<T> constructor;


    @Override
    public String toString() {
        return "EntityClassMetaDataImpl{" +
                ", clazz=" + clazz +
                ", className='" + className + '\'' +
                ", fieldsWithoutId=" + fieldsWithoutId +
                ", allFields=" + allFields +
                ", fieldWithId=" + fieldWithId +
                ", constructor=" + constructor +
                '}';
    }

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;

        parseObjectConstructor();
        parseObjectFields();
        parseFieldWithId();
        parseFieldsWithoutId();
        parseClassName();
    }

    @Override
    public String getName() {
        return this.className;
    }

    @Override
    public Constructor getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.fieldWithId;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }

    /**
     * Принимаем заданность, что конструктор только один
     */
    private void parseObjectConstructor() {
        try {
            Constructor<T> constructor = (Constructor<T>) this.clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            this.constructor = constructor;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Получение всех полей
     */
    private void parseObjectFields() {
        this.allFields = List.of(this.clazz.getDeclaredFields());
    }

    /**
     * Получение поля с аннотацией Id
     */
    private void parseFieldWithId() {

        for (Field field : this.allFields) {

            if (field.getAnnotation(Id.class) != null) {
                this.fieldWithId = field;
            }
        }
    }

    /**
     * Поучение полей без аннотации Id
     */
    private void parseFieldsWithoutId() {

        this.fieldsWithoutId = this.allFields.stream().filter(field -> {
            return field.getAnnotation(Id.class) == null;
        }).collect(Collectors.toList());

    }

    private void parseClassName() {

        this.className = this.clazz.getName();

    }

}
