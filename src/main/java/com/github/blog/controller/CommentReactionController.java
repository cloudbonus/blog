package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.CommentValidationGroups;
import com.github.blog.controller.util.marker.Marker;
import com.github.blog.service.CommentReactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raman Haurylau
 */
@Validated
@RestController
@RequestMapping("comment-reactions")
@RequiredArgsConstructor
public class CommentReactionController {
    private final CommentReactionService commentReactionService;

    @PostMapping
    @Validated(CommentValidationGroups.CommentReactionCreateValidationGroupSequence.class)
    public CommentReactionDto create(@RequestBody @Valid CommentReactionRequest request) {
        return commentReactionService.create(request);
    }

    @GetMapping("{id}")
    public CommentReactionDto findById(@PathVariable("id") @Positive Long id) {
        return commentReactionService.findById(id);
    }

    @GetMapping
    public PageResponse<CommentReactionDto> findAll(@Valid CommentReactionFilterRequest requestFilter, @Valid PageableRequest pageableRequest) {
        return commentReactionService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @Validated(Marker.onUpdate.class)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public CommentReactionDto update(@PathVariable("id") @P("id") @Positive Long id, @Valid @RequestBody CommentReactionRequest request) {
        return commentReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public CommentReactionDto delete(@PathVariable("id") @P("id") @Positive Long id) {
        return commentReactionService.delete(id);
    }
}

