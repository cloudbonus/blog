package com.github.blog.controller;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.security.impl.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
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
    public CommentReactionDto create(@RequestBody CommentReactionRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getUserId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
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
    public CommentReactionDto update(@PathVariable("id") Long id, @RequestBody CommentReactionRequest request, HttpServletRequest HttpRequest,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_UPDATE_ATTEMPT);
        }
        return commentReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    public CommentReactionDto delete(@PathVariable("id") Long id, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_DELETION_ATTEMPT);
        }
        return commentReactionService.delete(id);
    }
}

