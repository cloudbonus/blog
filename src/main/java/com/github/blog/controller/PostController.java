package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.service.PostService;
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

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'COMPANY') and #request.userId == authentication.principal.id")
    public PostDto create(@RequestBody @P("request") PostRequest request) {
        return postService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postAccess.verifyPostPurchase(#id)")
    public PostDto findById(@PathVariable @P("id") Long id) {
        return postService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @postAccess.canFilter(#request)")
    public Page<PostDto> findAll(@P("request") PostDtoFilter requestFilter, PageableRequest pageableRequest) {
        return postService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public PostDto update(@PathVariable("id") @P("id") Long id, @RequestBody PostRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public PostDto delete(@PathVariable("id") @P("id") Long id) {
        return postService.delete(id);
    }
}
