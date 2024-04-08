package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.CommentDto;
import com.github.blog.service.CommentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class CommentController {
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentController(CommentService commentService, ObjectMapper objectMapper) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    public String create(CommentDto commentDto) {
        return convertToJson(commentService.create(commentDto));
    }

    public String findById(int id) {
        return convertToJson(commentService.findById(id));
    }

    public String findAll() {
        List<CommentDto> comments = commentService.findAll();
        return convertToJsonArray(comments);
    }

    public String update(int id, CommentDto commentDto) {
        return convertToJson(commentService.update(id, commentDto));
    }

    public String remove(int id) {
        int result = commentService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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

