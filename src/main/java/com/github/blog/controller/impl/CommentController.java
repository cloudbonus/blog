package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.CommentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
/**
 * @author Raman Haurylau
 */
@Component
public class CommentController implements Controller<Serializable> {
    private final Service<Serializable> commentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentController(CommentService commentService, ObjectMapper objectMapper) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable commentDto) {
        return commentService.create(commentDto);
    }

    public String readById(int id) {
        return convertToJson(commentService.readById(id));
    }

    public String readAll() {
        List<Serializable> comments = commentService.readAll();
        return convertToJsonArray(comments);
    }

    public boolean update(int id, Serializable commentDto) {
        return commentService.update(id, commentDto);
    }

    public boolean delete(int id) {
        return commentService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable commentDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> comments) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments);
    }
}

