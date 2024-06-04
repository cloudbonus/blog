package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.CommentReactionDao;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.CommentReactionMapper;
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
public class CommentReactionServiceImpl implements CommentReactionService {
    private final UserDao userDao;
    private final CommentReactionDao commentReactionDao;
    private final CommentDao commentDao;
    private final ReactionDao reactionDao;

    private final CommentReactionMapper commentReactionMapper;
    private final PageableMapper pageableMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public CommentReactionDto create(CommentReactionRequest request) {
        log.debug("Creating a new comment reaction with request: {}", request);
        CommentReaction commentReaction = commentReactionMapper.toEntity(request);

        Comment comment = commentDao
                .findById(request.getCommentId())
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", request.getCommentId());
                    return new CustomException(ExceptionEnum.COMMENT_NOT_FOUND);
                });

        Reaction reaction = reactionDao
                .findById(request.getReactionId())
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", request.getReactionId());
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userAccessHandler.getUserId());
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        commentReaction.setReaction(reaction);
        commentReaction.setComment(comment);
        commentReaction.setUser(user);

        commentReaction = commentReactionDao.create(commentReaction);
        log.debug("Comment reaction created successfully with ID: {}", commentReaction.getId());

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto findById(Long id) {
        log.debug("Finding comment reaction by ID: {}", id);
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND);
                });

        log.debug("Comment reaction found with ID: {}", id);
        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public PageResponse<CommentReactionDto> findAll(CommentReactionFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all comment reactions with filter: {} and pageable: {}", requestFilter, pageableRequest);
        CommentReactionFilter filter = commentReactionMapper.toEntity(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<CommentReaction> commentReactions = commentReactionDao.findAll(filter, pageable);

        if (commentReactions.isEmpty()) {
            log.error("No comment reactions found with the given filter and pageable");
            throw new CustomException(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND);
        }

        log.info("Found {} comment reactions", commentReactions.getTotalNumberOfEntities());
        return commentReactionMapper.toDto(commentReactions);
    }

    @Override
    public CommentReactionDto update(Long id, CommentReactionRequest request) {
        log.debug("Updating comment reaction with ID: {} and request: {}", id, request);
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND);
                });

        Reaction reaction = reactionDao
                .findById(request.getReactionId())
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", request.getReactionId());
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        commentReaction.setReaction(reaction);
        log.debug("Comment reaction updated successfully with ID: {}", id);

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto delete(Long id) {
        log.debug("Deleting comment reaction with ID: {}", id);
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Comment reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND);
                });

        commentReactionDao.delete(commentReaction);
        log.debug("Post deleted successfully with ID: {}", id);

        return commentReactionMapper.toDto(commentReaction);
    }
}

