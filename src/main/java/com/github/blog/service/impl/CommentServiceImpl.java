package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.CommentUpdateRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentFilter;
import com.github.blog.service.CommentService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CommentException;
import com.github.blog.service.mapper.CommentMapper;
import com.github.blog.service.mapper.PageableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final CommentMapper commentMapper;
    private final PageableMapper pageableMapper;

    @Override
    public CommentDto create(CommentRequest request) {
        Comment comment = commentMapper.toEntity(request);
        return commentMapper.toDto(commentDao.create(comment));
    }

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_NOT_FOUND));

        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> findAll(CommentDtoFilter requestFilter, PageableRequest pageableRequest) {
        CommentFilter dtoFilter = commentMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<Comment> comments = commentDao.findAll(dtoFilter, pageable);

        if (comments.isEmpty()) {
            throw new CommentException(ExceptionEnum.COMMENTS_NOT_FOUND);
        }

        return comments.map(commentMapper::toDto);
    }

    @Override
    public CommentDto update(Long id, CommentUpdateRequest request) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_NOT_FOUND));

        comment = commentMapper.partialUpdate(request, comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto delete(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_NOT_FOUND));

        commentDao.delete(comment);

        return commentMapper.toDto(comment);
    }
}

