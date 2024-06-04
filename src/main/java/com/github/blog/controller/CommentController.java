package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.BaseMarker;
import com.github.blog.service.CommentService;
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
    @PreAuthorize("@orderAccess.isOrderCompleted(#request.postId)")
    public CommentDto create(@RequestBody @P("request") @Validated(BaseMarker.Create.class) CommentRequest request) {
        return commentService.create(request);
    }

    @GetMapping("{id}")
    public CommentDto findById(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return commentService.findById(id);
    }

    @GetMapping
    public PageResponse<CommentDto> findAll(@Validated CommentFilterRequest requestFilter, @Validated PageableRequest pageableRequest) {
        return commentService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentAccess.verifyOwnership(#id)")
    public CommentDto update(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id, @RequestBody @Validated(BaseMarker.Update.class) CommentRequest request) {
        return commentService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentAccess.verifyOwnership(#id)")
    public CommentDto delete(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id) {
        return commentService.delete(id);
    }
}

