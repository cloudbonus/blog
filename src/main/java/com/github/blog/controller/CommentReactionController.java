package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class CommentReactionController {
    private final CommentReactionService commentReactionService;
    private final JsonMapper jsonMapper;

    public String create(CommentReactionDto commentReactionDto) {
        return jsonMapper.convertToJson(commentReactionService.create(commentReactionDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(commentReactionService.findById(id));
    }

    public String findAll() {
        List<CommentReactionDto> commentReactions = commentReactionService.findAll();
        return jsonMapper.convertToJson(commentReactions);
    }

    public String update(Long id, CommentReactionDto commentReactionDto) {
        return jsonMapper.convertToJson(commentReactionService.update(id, commentReactionDto));
    }

    public void delete(Long id) {
        commentReactionService.delete(id);
    }
}

