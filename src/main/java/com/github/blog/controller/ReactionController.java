package com.github.blog.controller;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.service.ReactionService;
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
@RequestMapping("reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ReactionDto create(@RequestBody @Validated ReactionRequest request) {
        return reactionService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReactionDto findById(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return reactionService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ReactionDto> findAll(@Validated PageableRequest pageableRequest) {
        return reactionService.findAll(pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReactionDto update(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id, @RequestBody @Validated ReactionRequest request) {
        return reactionService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReactionDto delete(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return reactionService.delete(id);
    }
}
