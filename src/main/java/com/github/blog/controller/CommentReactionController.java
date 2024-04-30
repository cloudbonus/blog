package com.github.blog.controller;

import com.github.blog.dto.common.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
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
@RequestMapping("comment-reactions")
@RequiredArgsConstructor
public class CommentReactionController {
    private final CommentReactionService commentReactionService;

    @PostMapping
    public CommentReactionDto create(@RequestBody CommentReactionDto commentReactionDto) {
        return commentReactionService.create(commentReactionDto);
    }

    @GetMapping("{id}")
    public CommentReactionDto findById(@PathVariable("id") Long id) {
        return commentReactionService.findById(id);
    }

    @GetMapping
    public List<CommentReactionDto> findAll() {
        return commentReactionService.findAll();
    }

    @PutMapping("{id}")
    public CommentReactionDto update(@PathVariable("id") Long id, @RequestBody CommentReactionDto commentReactionDto) {
        return commentReactionService.update(id, commentReactionDto);
    }

    @DeleteMapping("{id}")
    public CommentReactionDto delete(@PathVariable("id") Long id) {
        return commentReactionService.delete(id);
    }
}

