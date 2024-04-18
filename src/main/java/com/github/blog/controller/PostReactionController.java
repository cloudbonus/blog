package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.service.PostReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;
    private final JsonMapper jsonMapper;

    public String create(PostReactionDto postReactionDto) {
        return jsonMapper.convertToJson(postReactionService.create(postReactionDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(postReactionService.findById(id));
    }

    public String findAll() {
        List<PostReactionDto> postReactions = postReactionService.findAll();
        return jsonMapper.convertToJson(postReactions);
    }

    public String update(Long id, PostReactionDto postReactionDto) {
        return jsonMapper.convertToJson(postReactionService.update(id, postReactionDto));
    }

    public void delete(Long id) {
        postReactionService.delete(id);
    }
}
