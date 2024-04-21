package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.CommentDto;
import com.github.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JsonMapper jsonMapper;

    public String create(CommentDto commentDto) {
        return jsonMapper.convertToJson(commentService.create(commentDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(commentService.findById(id));
    }

    public String findAll() {
        List<CommentDto> comments = commentService.findAll();
        return jsonMapper.convertToJson(comments);
    }

    public String update(Long id, CommentDto commentDto) {
        return jsonMapper.convertToJson(commentService.update(id, commentDto));
    }

    public void delete(Long id) {
        commentService.delete(id);
    }
}

