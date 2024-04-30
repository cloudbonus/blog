package com.github.blog.service.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.dto.common.CommentDto;
import com.github.blog.dto.filter.CommentDtoFilter;
import com.github.blog.dto.request.CommentRequestFilter;
import com.github.blog.model.Comment;
import com.github.blog.service.CommentService;
import com.github.blog.service.exception.CommentErrorResult;
import com.github.blog.service.exception.impl.CommentException;
import com.github.blog.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPublishedAt(OffsetDateTime.now());
        return commentMapper.toDto(commentDao.create(comment));
    }

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorResult.COMMENT_NOT_FOUND));

        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> findAll(CommentRequestFilter requestFilter) {
        CommentDtoFilter dtoFilter = commentMapper.toDto(requestFilter);

        List<Comment> comments = commentDao.findAll(dtoFilter);

        if (comments.isEmpty()) {
            throw new CommentException(CommentErrorResult.COMMENTS_NOT_FOUND);
        }

        return comments.stream().map(commentMapper::toDto).toList();
    }

    @Override
    public CommentDto update(Long id, CommentDto commentDto) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorResult.COMMENT_NOT_FOUND));

        comment = commentMapper.partialUpdate(commentDto, comment);
        comment = commentDao.update(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto delete(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorResult.COMMENT_NOT_FOUND));
        commentDao.delete(comment);
        return commentMapper.toDto(comment);
    }
}

