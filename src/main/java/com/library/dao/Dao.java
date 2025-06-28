package com.library.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void insert(T t);
    Optional<T> findById(String id);
    List<T> findAll();
    void update(T t);
    void delete(String id);
}