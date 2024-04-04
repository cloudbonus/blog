package com.github.blog.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface CrudDao<T> {
    Optional<T> getById(int id);

    List<T> getAll();

    int save(T t);

    Optional<T> update(T t);

    boolean deleteById(int id);
}
