package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.BaseMarker;
import com.github.blog.service.PostReactionService;
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
@RequestMapping("post-reactions")
@RequiredArgsConstructor
public class PostReactionController {

    private final PostReactionService postReactionService;

    @PostMapping
    public PostReactionDto create(@RequestBody @Validated(BaseMarker.onCreate.class) PostReactionRequest request) {
        return postReactionService.create(request);
    }

    @GetMapping("{id}")
    public PostReactionDto findById(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return postReactionService.findById(id);
    }

    @GetMapping
    public PageResponse<PostReactionDto> findAll(@Validated PostReactionFilterRequest filterRequest, @Validated PageableRequest pageableRequest) {
        return postReactionService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postReactionAccess.verifyOwnership(#id)")
    public PostReactionDto update(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id, @RequestBody @Validated(BaseMarker.onUpdate.class) PostReactionRequest request) {
        return postReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @postReactionAccess.verifyOwnership(#id)")
    public PostReactionDto delete(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id) {
        return postReactionService.delete(id);
    }
}
