package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.PostDto;
import com.github.blog.service.PostService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    public int create(PostDto postDto) {
        return postService.create(postDto);
    }

    public String readById(int id) {
        return convertToJson(postService.readById(id));
    }

    public String readAll() {
        List<PostDto> posts = postService.readAll();
        return convertToJsonArray(posts);
    }

    public PostDto update(int id, PostDto postDto) {
        return postService.update(id, postDto);
    }

    public boolean delete(int id) {
        return postService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(PostDto postDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<PostDto> posts) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(posts);
    }
}
