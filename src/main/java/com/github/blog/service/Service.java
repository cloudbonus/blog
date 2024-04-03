package com.github.blog.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface Service<T extends Serializable> {
    int create(T t);

    T readById(int id);

    List<T> readAll();

    boolean update(int id, T t);

    boolean delete(int id);
}
