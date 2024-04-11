package com.github.blog.controller;

import com.github.blog.dto.PostDto;
import com.github.blog.service.PostService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final DefaultMapper mapper;

    public String create(PostDto postDto) {
        return mapper.convertToJson(postService.create(postDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(postService.findById(id));
    }

    public String findAll() {
        List<PostDto> posts = postService.findAll();
        return mapper.convertToJson(posts);
    }

    public String update(int id, PostDto postDto) {
        return mapper.convertToJson(postService.update(id, postDto));
    }

    public String remove(int id) {
        int result = postService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }
}
