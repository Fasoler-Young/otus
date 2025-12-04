package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;
    private String selectAllSql = null;
    private String selectByIdSql = null;
    private String insertSql = null;
    private String updateSql = null;
    private String allFields = null;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaDataClient) {
        this.entityClassMetaData = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            selectAllSql = "select %s from %s".formatted(getAllFieldNames(), entityClassMetaData.getName());
        }
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdSql == null) {
            selectByIdSql = "select %s from %s where %s in (?)"
                    .formatted(
                            getAllFieldNames(),
                            entityClassMetaData.getName(),
                            entityClassMetaData.getIdField().getName());
        }
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null) {
            insertSql = "insert into %s (%s) values (%s)"
                    .formatted(
                            entityClassMetaData.getName(),
                            entityClassMetaData.getFieldsWithoutId().stream()
                                    .map(Field::getName)
                                    .collect(Collectors.joining(", ")),
                            entityClassMetaData.getFieldsWithoutId().stream()
                                    .map(field -> "?")
                                    .collect(Collectors.joining(", ")));
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (updateSql == null) {
            updateSql = "insert into %s (%s) values (%s)"
                    .formatted(
                            entityClassMetaData.getName(),
                            entityClassMetaData.getAllFields().stream()
                                    .map(Field::getName)
                                    .collect(Collectors.joining(", ")),
                            entityClassMetaData.getAllFields().stream()
                                    .map(field -> "?")
                                    .collect(Collectors.joining(", ")));
        }
        return updateSql;
    }

    private String getAllFieldNames() {
        if (allFields == null) {
            allFields = entityClassMetaData.getAllFields().stream()
                    .map(Field::getName)
                    .collect(Collectors.joining(", "));
        }
        return allFields;
    }
}
