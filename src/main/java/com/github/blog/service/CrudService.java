package com.github.blog.service;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CrudService<T> {
    T create(T t);

    T findById(int id);

    List<T> findAll();

    T update(int id, T t);

    int remove(int id);
}
