package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.service.CommentService;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto create(@RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getUserId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
        return commentService.create(request);
    }

    @GetMapping("{id}")
    public CommentDto findById(@PathVariable("id") Long id) {
        return commentService.findById(id);
    }

    @GetMapping
    public Page<CommentDto> findAll(CommentDtoFilter requestFilter, PageableRequest pageableRequest) {
        return commentService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    public CommentDto update(@PathVariable("id") Long id, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.isAdmin() && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_UPDATE_ATTEMPT);
        }
        return commentService.update(id, request);
    }

    @DeleteMapping("{id}")
    public CommentDto delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.isAdmin() && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_DELETION_ATTEMPT);
        }
        return commentService.delete(id);
    }
}

