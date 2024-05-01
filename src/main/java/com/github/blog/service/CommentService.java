package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface CommentService {
    Page<CommentDto> findAll(CommentDtoFilter requestFilter);

    CommentDto create(CommentDto t);

    CommentDto findById(Long id);

    CommentDto update(Long id, CommentDto t);

    CommentDto delete(Long id);
}
