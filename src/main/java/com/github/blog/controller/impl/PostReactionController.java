package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.impl.PostReactionService;
import com.github.blog.service.Service;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class PostReactionController implements Controller<Serializable> {
    private final Service<Serializable> postReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostReactionController(PostReactionService postReactionService, ObjectMapper objectMapper) {
        this.postReactionService = postReactionService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable postReactionDto) {
        return postReactionService.create(postReactionDto);
    }

    public String readById(int id) {
        return convertToJson(postReactionService.readById(id));
    }

    public String readAll() {
        List<Serializable> postReactions = postReactionService.readAll();
        return convertToJsonArray(postReactions);
    }

    public boolean update(int id, Serializable postReactionDto) {
        return postReactionService.update(id, postReactionDto);
    }

    public boolean delete(int id) {
        return postReactionService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable postReactionDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postReactionDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> postReactions) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postReactions);
    }
}
