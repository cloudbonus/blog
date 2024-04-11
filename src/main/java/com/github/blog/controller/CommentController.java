package com.github.blog.controller;

import com.github.blog.dto.CommentDto;
import com.github.blog.service.CommentService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final DefaultMapper mapper;

    public String create(CommentDto commentDto) {
        return mapper.convertToJson(commentService.create(commentDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(commentService.findById(id));
    }

    public String findAll() {
        List<CommentDto> comments = commentService.findAll();
        return mapper.convertToJson(comments);
    }

    public String update(int id, CommentDto commentDto) {
        return mapper.convertToJson(commentService.update(id, commentDto));
    }

    public String remove(int id) {
        int result = commentService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }
}

