package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.service.PostService;
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

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    //@PreAuthorize("authentication.principal.id == request.userId")
    public PostDto create(@RequestBody PostRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getUserId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
        return postService.create(request);
    }

    @GetMapping("{id}")
    public PostDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping
    public Page<PostDto> findAll(PostDtoFilter requestFilter, PageableRequest pageableRequest) {
        return postService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    public PostDto update(@PathVariable("id") Long id, @RequestBody PostRequest request, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_UPDATE_ATTEMPT);
        }
        return postService.update(id, request);
    }

    @DeleteMapping("{id}")
    public PostDto delete(@PathVariable("id") Long id, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_DELETION_ATTEMPT);
        }
        return postService.delete(id);
    }
}
