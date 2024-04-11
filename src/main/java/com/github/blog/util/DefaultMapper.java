package com.github.blog.util;

/**
 * @author Raman Haurylau
 */
public interface DefaultMapper {
    <T, U> T map(U source, Class<T> targetClass);

    <T> String convertToJson(T object);
}
