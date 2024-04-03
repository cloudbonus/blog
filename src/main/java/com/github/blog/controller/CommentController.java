package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.CommentDto;
import com.github.blog.service.CommentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentController {
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentController(CommentService commentService, ObjectMapper objectMapper) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    public int create(CommentDto commentDto) {
        return commentService.create(commentDto);
    }

    public String readById(int id) {
        return convertToJson(commentService.readById(id));
    }

    public String readAll() {
        List<CommentDto> comments = commentService.readAll();
        return convertToJsonArray(comments);
    }

    public CommentDto update(int id, CommentDto commentDto) {
        return commentService.update(id, commentDto);
    }

    public boolean delete(int id) {
        return commentService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(CommentDto commentDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<CommentDto> comments) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments);
    }
}

