package ru.otus.dao;

import java.util.Optional;

public interface EntityDao<T> {

    Optional<T> findById(long id);

    Optional<T> findRandom();
}
