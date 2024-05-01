package com.github.blog.service;

import com.github.blog.dto.common.CommentDto;
import com.github.blog.dto.request.CommentDtoFilter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentService {
    List<CommentDto> findAll(CommentDtoFilter requestFilter);

    CommentDto create(CommentDto t);

    CommentDto findById(Long id);

    CommentDto update(Long id, CommentDto t);

    CommentDto delete(Long id);
}
