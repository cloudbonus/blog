package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.TagDto;
import com.github.blog.service.TagService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class TagController {
    private final TagService tagService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagController(TagService tagService, ObjectMapper objectMapper) {
        this.tagService = tagService;
        this.objectMapper = objectMapper;
    }

    public String create(TagDto tagDto) {
        return convertToJson(tagService.create(tagDto));
    }

    public String findById(int id) {
        return convertToJson(tagService.findById(id));
    }

    public String findAll() {
        List<TagDto> tags = tagService.findAll();
        return convertToJsonArray(tags);
    }

    public String update(int id, TagDto tagDto) {
        return convertToJson(tagService.update(id, tagDto));
    }

    public String remove(int id) {
        int result = tagService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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
