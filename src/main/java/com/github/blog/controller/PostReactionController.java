package com.github.blog.controller;

import com.github.blog.dto.PostReactionDto;
import com.github.blog.mapper.Mapper;
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
    private final Mapper mapper;

    public String create(PostReactionDto postReactionDto) {
        return mapper.convertToJson(postReactionService.create(postReactionDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(postReactionService.findById(id));
    }

    public String findAll() {
        List<PostReactionDto> postReactions = postReactionService.findAll();
        return mapper.convertToJson(postReactions);
    }

    public String update(int id, PostReactionDto postReactionDto) {
        return mapper.convertToJson(postReactionService.update(id, postReactionDto));
    }

    public String remove(int id) {
        int result = postReactionService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }

}
