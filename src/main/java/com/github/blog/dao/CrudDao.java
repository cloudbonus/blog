package com.github.blog.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface CrudDao<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T create(T t);

    T update(T t);

    T remove(ID id);
}
