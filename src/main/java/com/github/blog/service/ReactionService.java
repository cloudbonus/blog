package com.github.blog.service;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface ReactionService {
    PageResponse<ReactionDto> findAll(PageableRequest pageableRequest);

    ReactionDto create(ReactionRequest t);

    ReactionDto findById(Long id);

    ReactionDto update(Long id, ReactionRequest t);

    ReactionDto delete(Long id);
}
