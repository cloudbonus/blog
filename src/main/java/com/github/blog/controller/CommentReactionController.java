package com.github.blog.controller;

import com.github.blog.dto.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
import com.github.blog.util.DefaultMapper;
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
    private final DefaultMapper mapper;

    public String create(CommentReactionDto commentReactionDto) {
        return mapper.convertToJson(commentReactionService.create(commentReactionDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(commentReactionService.findById(id));
    }

    public String findAll() {
        List<CommentReactionDto> commentReactions = commentReactionService.findAll();
        return mapper.convertToJson(commentReactions);
    }

    public String update(int id, CommentReactionDto commentReactionDto) {
        return mapper.convertToJson(commentReactionService.update(id, commentReactionDto));
    }

    public String remove(int id) {
        int result = commentReactionService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }
}

