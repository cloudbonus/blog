package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.CommentReactionDao;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CommentException;
import com.github.blog.service.exception.impl.CommentReactionException;
import com.github.blog.service.exception.impl.ReactionException;
import com.github.blog.service.mapper.CommentReactionMapper;
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
public class CommentReactionServiceImpl implements CommentReactionService {
    private final CommentReactionDao commentReactionDao;
    private final CommentReactionMapper commentReactionMapper;
    private final PageableMapper pageableMapper;
    private final CommentDao commentDao;
    private final ReactionDao reactionDao;

    @Override
    public CommentReactionDto create(CommentReactionRequest request) {
        CommentReaction commentReaction = commentReactionMapper.toEntity(request);

        Comment comment = commentDao
                .findById(commentReaction.getComment().getId())
                .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(commentReaction.getReaction().getId())
                .orElseThrow(() -> new ReactionException(ExceptionEnum.REACTION_NOT_FOUND));

        commentReaction.setReaction(reaction);
        commentReaction.setComment(comment);

        commentReaction = commentReactionDao.create(commentReaction);

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto findById(Long id) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CommentReactionException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public Page<CommentReactionDto> findAll(CommentReactionDtoFilter requestFilter, PageableRequest pageableRequest) {
        CommentReactionFilter dtoFilter = commentReactionMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<CommentReaction> commentReactions = commentReactionDao.findAll(dtoFilter, pageable);

        if (commentReactions.isEmpty()) {
            throw new CommentReactionException(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND);
        }

        return commentReactions.map(commentReactionMapper::toDto);
    }

    @Override
    public CommentReactionDto update(Long id, CommentReactionRequest request) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CommentReactionException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        commentReaction = commentReactionMapper.partialUpdate(request, commentReaction);

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto delete(Long id) {
        CommentReaction commentReaction = commentReactionDao
                .findById(id)
                .orElseThrow(() -> new CommentReactionException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        commentReactionDao.delete(commentReaction);

        return commentReactionMapper.toDto(commentReaction);
    }
}

