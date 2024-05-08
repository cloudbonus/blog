package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface CommentService {
    Page<CommentDto> findAll(CommentDtoFilter requestFilter, PageableRequest pageableRequest);

    CommentDto create(CommentRequest t);

    CommentDto findById(Long id);

    CommentDto update(Long id, CommentRequest t);

    CommentDto delete(Long id);
}
