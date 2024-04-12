package com.github.blog.mapper;

/**
 * @author Raman Haurylau
 */
public interface Mapper {
    <T, U> T map(U source, Class<T> targetClass);

    <T> String convertToJson(T object);
}
