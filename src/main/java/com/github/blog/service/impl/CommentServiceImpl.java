package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Log4j2
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
        log.debug("Creating a new comment with request: {}", request);
        Comment comment = commentMapper.toEntity(request);
        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userAccessHandler.getUserId());
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        Post post = postDao
                .findById(request.getPostId())
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", request.getPostId());
                    return new CustomException(ExceptionEnum.POST_NOT_FOUND);
                });

        comment.setUser(user);
        comment.setPost(post);

        Comment createdComment = commentDao.create(comment);
        log.debug("Created comment: {}", createdComment);

        return commentMapper.toDto(createdComment);
    }

    @Override
    public CommentDto findById(Long id) {
        log.debug("Finding comment by ID: {}", id);
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_NOT_FOUND);
                });

        log.debug("Comment found with ID: {}", id);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public PageResponse<CommentDto> findAll(CommentFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all comments with filter: {} and pageable: {}", requestFilter, pageableRequest);
        CommentFilter filter = commentMapper.toEntity(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Comment> comments = commentDao.findAll(filter, pageable);

        if (comments.isEmpty()) {
            log.error("No comments found with the given filter and pageable");
            throw new CustomException(ExceptionEnum.COMMENTS_NOT_FOUND);
        }

        log.info("Found {} comments", comments.getTotalNumberOfEntities());
        return commentMapper.toDto(comments);
    }

    @Override
    public CommentDto update(Long id, CommentRequest request) {
        log.debug("Updating comment with comments: {} and request: {}", id, request);
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_NOT_FOUND);
                });

        comment = commentMapper.partialUpdate(request, comment);
        log.debug("Comment updated successfully with ID: {}", id);

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto delete(Long id) {
        log.debug("Deleting comment with ID: {}", id);
        Comment comment = commentDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_NOT_FOUND);
                });

        commentDao.delete(comment);
        log.debug("Comment deleted successfully with ID: {}", id);

        return commentMapper.toDto(comment);
    }
}

