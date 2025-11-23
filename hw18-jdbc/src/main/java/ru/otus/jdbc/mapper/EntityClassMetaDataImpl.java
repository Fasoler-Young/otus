package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entityClass;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {
        return entityClass.getConstructor();
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(entityClass.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
