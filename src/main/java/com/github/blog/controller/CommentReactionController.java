package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class CommentReactionController {
    private final CommentReactionService commentReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionController(CommentReactionService commentReactionService, ObjectMapper objectMapper) {
        this.commentReactionService = commentReactionService;
        this.objectMapper = objectMapper;
    }

    public String create(CommentReactionDto commentReactionDto) {
        return convertToJson(commentReactionService.create(commentReactionDto));
    }

    public String findById(int id) {
        return convertToJson(commentReactionService.findById(id));
    }

    public String findAll() {
        List<CommentReactionDto> commentReactions = commentReactionService.findAll();
        return convertToJsonArray(commentReactions);
    }

    public String update(int id, CommentReactionDto commentReactionDto) {
        return convertToJson(commentReactionService.update(id, commentReactionDto));
    }

    public String remove(int id) {
        int result = commentReactionService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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

