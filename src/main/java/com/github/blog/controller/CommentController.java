package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentDtoFilter;
import com.github.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto create(@RequestBody CommentDto commentDto) {
        return commentService.create(commentDto);
    }

    @GetMapping("{id}")
    public CommentDto findById(@PathVariable("id") Long id) {
        return commentService.findById(id);
    }

    @GetMapping
    public List<CommentDto> findAll(CommentDtoFilter requestFilter) {
        return commentService.findAll(requestFilter);
    }

    @PutMapping("{id}")
    public CommentDto update(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
        return commentService.update(id, commentDto);
    }

    @DeleteMapping("{id}")
    public CommentDto delete(@PathVariable("id") Long id) {
        return commentService.delete(id);
    }
}

