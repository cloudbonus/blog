package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.TagService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class TagController implements Controller<Serializable> {
    private final Service<Serializable> tagService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagController(TagService tagService, ObjectMapper objectMapper) {
        this.tagService = tagService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable tagDto) {
        return tagService.create(tagDto);
    }

    public String readById(int id) {
        return convertToJson(tagService.readById(id));
    }

    public String readAll() {
        List<Serializable> tags = tagService.readAll();
        return convertToJsonArray(tags);
    }

    public boolean update(int id, Serializable tagDto) {
        return tagService.update(id, tagDto);
    }

    public boolean delete(int id) {
        return tagService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable tagDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tagDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> tags) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tags);
    }
}
