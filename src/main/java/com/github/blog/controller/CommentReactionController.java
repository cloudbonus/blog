package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.service.CommentReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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
    @PreAuthorize("#r.userId == authentication.principal.id")
    public CommentReactionDto create(@RequestBody @P("r") CommentReactionRequest request) {
        return commentReactionService.create(request);
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
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public CommentReactionDto update(@PathVariable("id") @P("id") Long id, @RequestBody CommentReactionRequest request) {
        return commentReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public CommentReactionDto delete(@PathVariable("id") @P("id") Long id) {
        return commentReactionService.delete(id);
    }
}

