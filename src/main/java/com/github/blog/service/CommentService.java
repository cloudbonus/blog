package com.github.blog.service;

import com.github.blog.dto.common.CommentDto;
import com.github.blog.dto.filter.CommentFilter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentService {
    List<CommentDto> findAll(CommentFilter filter);

    CommentDto create(CommentDto t);

    CommentDto findById(Long id);

    CommentDto update(Long id, CommentDto t);

    CommentDto delete(Long id);
}
