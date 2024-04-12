package com.github.blog.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.mapper.Mapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@AllArgsConstructor
@Component
public class DefaultMapperImpl implements Mapper {
    private final ObjectMapper objectMapper;

    @Override
    public <T, U> T map(U source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }

    @SneakyThrows
    public <T> String convertToJson(T object) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
