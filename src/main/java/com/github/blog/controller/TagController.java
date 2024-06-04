package com.github.blog.controller;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.service.TagService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TagDto create(@RequestBody @Validated TagRequest request) {
        return tagService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TagDto findById(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return tagService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<TagDto> findAll(@Validated TagFilterRequest filterRequest, @Validated PageableRequest pageableRequest) {
        return tagService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TagDto update(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id, @RequestBody @Validated TagRequest request) {
        return tagService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TagDto delete(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return tagService.delete(id);
    }
}
