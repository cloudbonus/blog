package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface CommentService {
    PageResponse<CommentDto> findAll(CommentFilterRequest requestFilter, PageableRequest pageableRequest);

    CommentDto create(CommentRequest t);

    CommentDto findById(Long id);

    CommentDto update(Long id, CommentRequest t);

    CommentDto delete(Long id);
}
