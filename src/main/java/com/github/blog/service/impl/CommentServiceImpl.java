package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentFilter;
import com.github.blog.service.CommentService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.CommentMapper;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.util.UserAccessHandler;
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
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final PostDao postDao;

    private final CommentMapper commentMapper;
    private final PageableMapper pageableMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public CommentDto create(CommentRequest request) {
        Comment comment = commentMapper.toEntity(request);
        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        Post post = postDao
                .findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        comment.setUser(user);
        comment.setPost(post);

        return commentMapper.toDto(commentDao.create(comment));
    }

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentDto> findAll(CommentFilterRequest requestFilter, PageableRequest pageableRequest) {
        CommentFilter dtoFilter = commentMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Comment> comments = commentDao.findAll(dtoFilter, pageable);

        if (comments.isEmpty()) {
            throw new CustomException(ExceptionEnum.COMMENTS_NOT_FOUND);
        }

        return commentMapper.toDto(comments);
    }

    @Override
    public CommentDto update(Long id, CommentRequest request) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

        comment = commentMapper.partialUpdate(request, comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto delete(Long id) {
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

        commentDao.delete(comment);

        return commentMapper.toDto(comment);
    }
}

