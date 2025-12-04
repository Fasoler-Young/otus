package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entityClass;
    private String name;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> allFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String getName() {
        if (name == null) {
            name = entityClass.getSimpleName().toLowerCase();
        }
        return name;
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {
        if (constructor == null) {
            constructor = entityClass.getConstructor();
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            idField = getAllFields().stream()
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElse(null);
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = Arrays.asList(entityClass.getDeclaredFields());
        }
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (allFieldsWithoutId == null) {
            allFieldsWithoutId = getAllFields().stream()
                    .filter(field -> !field.isAnnotationPresent(Id.class))
                    .collect(Collectors.toList());
        }
        return allFieldsWithoutId;
    }
}
