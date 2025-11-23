package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        this.entityClassMetaData = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from %s".formatted(entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from %s where %s in (?)"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return "insert into %s (%s) values (%s)"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Field::getName)
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(field -> "?")
                                .collect(Collectors.joining(", ")));
    }

    @Override
    public String getUpdateSql() {
        return "insert into %s (%s) values (%s)"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getAllFields().stream()
                                .map(Field::getName)
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getAllFields().stream()
                                .map(field -> "?")
                                .collect(Collectors.joining(", ")));
    }

    @Override
    public EntityClassMetaData<T> getEntityClassMetaData() {
        return entityClassMetaData;
    }
}
