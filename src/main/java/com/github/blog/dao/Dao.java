package com.github.blog.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface Dao<T> {
    Optional<T> getById(int id);

    List<T> getAll();

    int save(T t);

    boolean update(T t);

    boolean deleteById(int id);
}
