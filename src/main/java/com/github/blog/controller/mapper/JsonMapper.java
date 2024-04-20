package com.github.blog.controller.mapper;

/**
 * @author Raman Haurylau
 */
public interface JsonMapper {
    <T> String convertToJson(T object);
    <T> T convertToDto(String json, Class<T> clazz);
}
