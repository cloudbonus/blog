package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.service.PostReactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class PostReactionController {
    private final PostReactionService postReactionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostReactionController(PostReactionService postReactionService, ObjectMapper objectMapper) {
        this.postReactionService = postReactionService;
        this.objectMapper = objectMapper;
    }

    public String create(PostReactionDto postReactionDto) {
        return convertToJson(postReactionService.create(postReactionDto));
    }

    public String findById(int id) {
        return convertToJson(postReactionService.findById(id));
    }

    public String findAll() {
        List<PostReactionDto> postReactions = postReactionService.findAll();
        return convertToJsonArray(postReactions);
    }

    public String update(int id, PostReactionDto postReactionDto) {
        return convertToJson(postReactionService.update(id, postReactionDto));
    }

    public String remove(int id) {
        int result = postReactionService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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
