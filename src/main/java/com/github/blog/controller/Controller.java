package com.github.blog.controller;

import java.io.Serializable;

/**
 * @author Raman Haurylau
 */
public interface Controller<T extends Serializable> {

    int create(T t);

    String readById(int id);

    String readAll();

    boolean update(int id, T t);

    boolean delete(int id);
}
