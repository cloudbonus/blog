package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.PostService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class PostController implements Controller<Serializable> {
    private final Service<Serializable> postService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable postDto) {
        return postService.create(postDto);
    }

    public String readById(int id) {
        return convertToJson(postService.readById(id));
    }

    public String readAll() {
        List<Serializable> posts = postService.readAll();
        return convertToJsonArray(posts);
    }

    public boolean update(int id, Serializable postDto) {
        return postService.update(id, postDto);
    }

    public boolean delete(int id) {
        return postService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable postDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> posts) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(posts);
    }
}
