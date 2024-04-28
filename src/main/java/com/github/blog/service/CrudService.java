package com.github.blog.service;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CrudService<T, ID> {
    T create(T t);

    T findById(ID id);

    List<T> findAll();

    T update(ID id, T t);

    T delete(ID id);
}
