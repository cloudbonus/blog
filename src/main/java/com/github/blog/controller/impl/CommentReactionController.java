package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.CommentReactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionController implements Controller<Serializable> {
    private final Service<Serializable> commentReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionController(CommentReactionService commentReactionService, ObjectMapper objectMapper) {
        this.commentReactionService = commentReactionService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable commentReactionDto) {
        return commentReactionService.create(commentReactionDto);
    }

    public String readById(int id) {
        return convertToJson(commentReactionService.readById(id));
    }

    public String readAll() {
        List<Serializable> commentReactions = commentReactionService.readAll();
        return convertToJsonArray(commentReactions);
    }

    public boolean update(int id, Serializable commentReactionDto) {
        return commentReactionService.update(id, commentReactionDto);
    }

    public boolean delete(int id) {
        return commentReactionService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable commentReactionDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentReactionDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> commentReactions) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentReactions);
    }
}

