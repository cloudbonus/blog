package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
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
        CommentReaction commentReaction = commentReactionMapper.toEntity(request);

        Comment comment = commentDao
                .findById(commentReaction.getComment().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(commentReaction.getReaction().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        commentReaction.setReaction(reaction);
        commentReaction.setComment(comment);
        commentReaction.setUser(user);

        commentReaction = commentReactionDao.create(commentReaction);

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto findById(Long id) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public PageResponse<CommentReactionDto> findAll(CommentReactionFilterRequest requestFilter, PageableRequest pageableRequest) {
        CommentReactionFilter dtoFilter = commentReactionMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<CommentReaction> commentReactions = commentReactionDao.findAll(dtoFilter, pageable);

        if (commentReactions.isEmpty()) {
            throw new CustomException(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND);
        }

        return commentReactionMapper.toDto(commentReactions);
    }

    @Override
    public CommentReactionDto update(Long id, CommentReactionRequest request) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(request.getReactionId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        commentReaction.setReaction(reaction);
        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto delete(Long id) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        commentReactionDao.delete(commentReaction);

        return commentReactionMapper.toDto(commentReaction);
    }
}

