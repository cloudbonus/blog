package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.Marker;
import com.github.blog.service.PostService;
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
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @Validated(Marker.onCreate.class)
    @PreAuthorize("hasAnyRole('STUDENT', 'COMPANY', 'ADMIN')")
    public PostDto create(@RequestBody @Valid PostRequest request) {
        return postService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postAccess.verifyPostPurchase(#id)")
    public PostDto findById(@PathVariable("id") @P("id") @Positive Long id) {
        return postService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @postAccess.canFilter(#request)")
    public PageResponse<PostDto> findAll(@P("request") @Valid  PostFilterRequest requestFilter, @Valid PageableRequest pageableRequest) {
        return postService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postAccess.verifyOwner(#id)")
    public PostDto update(@PathVariable("id") @P("id") @Positive Long id, @RequestBody @Valid PostRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postAccess.verifyOwner(#id)")
    public PostDto delete(@PathVariable("id") @P("id") @Positive Long id) {
        return postService.delete(id);
    }
}
