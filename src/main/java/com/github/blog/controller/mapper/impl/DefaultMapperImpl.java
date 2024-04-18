package com.github.blog.controller.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.mapper.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@AllArgsConstructor
@Component
public class DefaultMapperImpl implements JsonMapper {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> String convertToJson(T object) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
