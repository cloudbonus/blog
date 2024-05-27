package com.github.blog.controller;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.Marker;
import com.github.blog.controller.util.marker.PostValidationGroups;
import com.github.blog.service.PostReactionService;
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
@RequestMapping("post-reactions")
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    @PostMapping
    @Validated(PostValidationGroups.PostReactionCreateValidationGroupSequence.class)
    public PostReactionDto create(@RequestBody @Valid PostReactionRequest request) {
        return postReactionService.create(request);
    }

    @GetMapping("{id}")
    public PostReactionDto findById(@PathVariable("id") @Positive Long id) {
        return postReactionService.findById(id);
    }

    @GetMapping
    public PageResponse<PostReactionDto> findAll(@Valid PostReactionFilterRequest filterRequest, @Valid PageableRequest pageableRequest) {
        return postReactionService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @Validated(Marker.onUpdate.class)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public PostReactionDto update(@PathVariable("id") @P("id") @Positive Long id, @RequestBody @Valid PostReactionRequest request) {
        return postReactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public PostReactionDto delete(@PathVariable("id") @P("id") @Positive Long id) {
        return postReactionService.delete(id);
    }
}
