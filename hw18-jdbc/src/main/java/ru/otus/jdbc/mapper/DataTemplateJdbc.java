package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData<T> entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return buildObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var clientList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            T object = buildObject(rs);
                            clientList.add(object);
                        }
                        return clientList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private T buildObject(ResultSet rs) throws SQLException {
        try {
            T object = entityClassMetaData.getConstructor().newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(object, rs.getObject(field.getName()));
            }
            return object;
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long insert(Connection connection, T client) {
        return save(connection, client, true);
    }

    @Override
    public void update(Connection connection, T client) {
        save(connection, client, false);
    }

    private long save(Connection connection, T client, boolean insert) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getValues(client, insert));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getValues(T client, boolean without) {
        List<Object> values = new ArrayList<>();
        for (Field field : client.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class) && without) {
                continue;
            }
            field.setAccessible(true);
            try {
                values.add(field.get(client));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return values;
    }
}
