package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.PostDto;
import com.github.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JsonMapper jsonMapper;

    public String create(PostDto postDto) {
        return jsonMapper.convertToJson(postService.create(postDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(postService.findById(id));
    }

    public String findAll() {
        List<PostDto> posts = postService.findAll();
        return jsonMapper.convertToJson(posts);
    }

    public String update(Long id, PostDto postDto) {
        return jsonMapper.convertToJson(postService.update(id, postDto));
    }

    public void delete(Long id) {
        postService.delete(id);
    }
}
