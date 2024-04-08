package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.PostDto;
import com.github.blog.service.PostService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    public String create(PostDto postDto) {
        return convertToJson(postService.create(postDto));
    }

    public String findById(int id) {
        return convertToJson(postService.findById(id));
    }

    public String findAll() {
        List<PostDto> posts = postService.findAll();
        return convertToJsonArray(posts);
    }

    public String update(int id, PostDto postDto) {
        return convertToJson(postService.update(id, postDto));
    }

    public String remove(int id) {
        int result = postService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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
