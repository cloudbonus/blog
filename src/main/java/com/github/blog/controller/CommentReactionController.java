package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionController {
    private final CommentReactionService commentReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionController(CommentReactionService commentReactionService, ObjectMapper objectMapper) {
        this.commentReactionService = commentReactionService;
        this.objectMapper = objectMapper;
    }

    public int create(CommentReactionDto commentReactionDto) {
        return commentReactionService.create(commentReactionDto);
    }

    public String readById(int id) {
        return convertToJson(commentReactionService.readById(id));
    }

    public String readAll() {
        List<CommentReactionDto> commentReactions = commentReactionService.readAll();
        return convertToJsonArray(commentReactions);
    }

    public CommentReactionDto update(int id, CommentReactionDto commentReactionDto) {
        return commentReactionService.update(id, commentReactionDto);
    }

    public boolean delete(int id) {
        return commentReactionService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(CommentReactionDto commentReactionDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentReactionDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<CommentReactionDto> commentReactions) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(commentReactions);
    }
}

