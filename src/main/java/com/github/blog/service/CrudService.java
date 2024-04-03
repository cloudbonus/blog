package com.github.blog.service;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CrudService<T> {
    int create(T t);

    T readById(int id);

    List<T> readAll();

    T update(int id, T t);

    boolean delete(int id);
}
