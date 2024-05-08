package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.service.PostReactionService;
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
@RequestMapping("post-reactions")
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    @PostMapping
    public PostReactionDto create(@RequestBody PostReactionRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getUserId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
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
    public PostReactionDto update(@PathVariable("id") Long id, @RequestBody PostReactionRequest request, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_UPDATE_ATTEMPT);
        }
        return postReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    public PostReactionDto delete(@PathVariable("id") Long id, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_DELETION_ATTEMPT);
        }
        return postReactionService.delete(id);
    }
}
