package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.TagDto;
import com.github.blog.service.TagService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class TagController {
    private final TagService tagService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagController(TagService tagService, ObjectMapper objectMapper) {
        this.tagService = tagService;
        this.objectMapper = objectMapper;
    }

    public int create(TagDto tagDto) {
        return tagService.create(tagDto);
    }

    public String readById(int id) {
        return convertToJson(tagService.readById(id));
    }

    public String readAll() {
        List<TagDto> tags = tagService.readAll();
        return convertToJsonArray(tags);
    }

    public TagDto update(int id, TagDto tagDto) {
        return tagService.update(id, tagDto);
    }

    public boolean delete(int id) {
        return tagService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(TagDto tagDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tagDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<TagDto> tags) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tags);
    }
}
