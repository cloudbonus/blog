package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.service.PostReactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class PostReactionController {
    private final PostReactionService postReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostReactionController(PostReactionService postReactionService, ObjectMapper objectMapper) {
        this.postReactionService = postReactionService;
        this.objectMapper = objectMapper;
    }

    public int create(PostReactionDto postReactionDto) {
        return postReactionService.create(postReactionDto);
    }

    public String readById(int id) {
        return convertToJson(postReactionService.readById(id));
    }

    public String readAll() {
        List<PostReactionDto> postReactions = postReactionService.readAll();
        return convertToJsonArray(postReactions);
    }

    public PostReactionDto update(int id, PostReactionDto postReactionDto) {
        return postReactionService.update(id, postReactionDto);
    }

    public boolean delete(int id) {
        return postReactionService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(PostReactionDto postReactionDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postReactionDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<PostReactionDto> postReactions) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postReactions);
    }
}
