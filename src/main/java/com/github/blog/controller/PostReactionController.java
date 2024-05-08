package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.service.PostReactionService;
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
@RequestMapping("post-reactions")
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    @PostMapping
    @PreAuthorize("#r.userId == authentication.principal.id")
    public PostReactionDto create(@RequestBody @P("r") PostReactionRequest request) {
        return postReactionService.create(request);
    }

    @GetMapping("{id}")
    public PostReactionDto findById(@PathVariable("id") Long id) {
        return postReactionService.findById(id);
    }

    @GetMapping
    public List<PostReactionDto> findAll() {
        return postReactionService.findAll();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public PostReactionDto update(@PathVariable("id") @P("id") Long id, @RequestBody PostReactionRequest request) {
        return postReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public PostReactionDto delete(@PathVariable("id") @P("id") Long id) {
        return postReactionService.delete(id);
    }
}
