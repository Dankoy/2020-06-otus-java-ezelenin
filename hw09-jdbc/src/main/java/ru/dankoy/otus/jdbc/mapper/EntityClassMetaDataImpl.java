package ru.dankoy.otus.jdbc.mapper;

import ru.dankoy.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl implements EntityClassMetaData {

    private Object object;
    private Class<?> clazz;
    private String className;
    private List<Field> fieldsWithoutId;
    private List<Field> allFields;
    private Field fieldWithId;
    private Constructor constructor;


    @Override
    public String toString() {
        return "EntityClassMetaDataImpl{" +
                "object=" + object +
                ", clazz=" + clazz +
                ", className='" + className + '\'' +
                ", fieldsWithoutId=" + fieldsWithoutId +
                ", allFields=" + allFields +
                ", fieldWithId=" + fieldWithId +
                ", constructor=" + constructor +
                '}';
    }

    public EntityClassMetaDataImpl(Object object) {
        this.object = object;
        this.clazz = object.getClass();

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
        Constructor[] constructors = this.clazz.getConstructors();
        this.constructor = constructors[0];
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

        this.fieldsWithoutId  = this.allFields.stream().filter(field -> {
            return field.getAnnotation(Id.class) == null;
        }).collect(Collectors.toList());

    }

    private void parseClassName() {

        this.className = this.clazz.getSimpleName();

    }

}
