package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface CommentReactionService {
    PageResponse<CommentReactionDto> findAll(CommentReactionFilterRequest requestFilter, PageableRequest pageableRequest);

    CommentReactionDto create(CommentReactionRequest t);

    CommentReactionDto findById(Long id);

    CommentReactionDto update(Long id, CommentReactionRequest t);

    CommentReactionDto delete(Long id);
}
