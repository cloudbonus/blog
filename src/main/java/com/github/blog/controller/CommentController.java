package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.Marker;
import com.github.blog.service.CommentService;
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
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Validated({Marker.onCreate.class})
    @PreAuthorize("@commentAccess.verifyPostPurchase(#request.postId)")
    public CommentDto create(@RequestBody @P("request") @Valid CommentRequest request) {
        return commentService.create(request);
    }

    @GetMapping("{id}")
    public CommentDto findById(@PathVariable("id") @Positive Long id) {
        return commentService.findById(id);
    }

    @GetMapping
    public PageResponse<CommentDto> findAll(@Valid CommentFilterRequest requestFilter, @Valid PageableRequest pageableRequest) {
        return commentService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @Validated({Marker.onUpdate.class})
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public CommentDto update(@PathVariable("id") @P("id") @Positive Long id, @RequestBody @Valid CommentRequest request) {
        return commentService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public CommentDto delete(@PathVariable("id") @P("id") @Positive Long id) {
        return commentService.delete(id);
    }
}

