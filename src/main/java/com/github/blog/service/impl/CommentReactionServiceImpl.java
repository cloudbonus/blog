package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.CommentReactionRepository;
import com.github.blog.repository.CommentRepository;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.entity.Comment;
import com.github.blog.repository.entity.CommentReaction;
import com.github.blog.repository.entity.Reaction;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.filter.CommentReactionFilter;
import com.github.blog.repository.specification.CommentReactionSpecification;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.CommentReactionMapper;
import com.github.blog.service.util.UserAccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserRepository userRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    private final CommentReactionMapper commentReactionMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public CommentReactionDto create(CommentReactionRequest request) {
        log.debug("Creating a new comment reaction with request: {}", request);
        CommentReaction commentReaction = commentReactionMapper.toEntity(request);

        Comment comment = commentRepository
                .findById(request.commentId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

        Reaction reaction = reactionRepository
                .findById(request.reactionId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        User user = userRepository
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        commentReaction.setReaction(reaction);
        commentReaction.setComment(comment);
        commentReaction.setUser(user);

        commentReaction = commentReactionRepository.save(commentReaction);
        log.debug("Comment reaction created successfully with ID: {}", commentReaction.getId());

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentReactionDto findById(Long id) {
        log.debug("Finding comment reaction by ID: {}", id);
        CommentReaction commentReaction = commentReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        log.debug("Comment reaction found with ID: {}", id);
        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentReactionDto> findAll(CommentReactionFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all comment reactions with filter: {} and pageable: {}", requestFilter, pageableRequest);
        CommentReactionFilter filter = commentReactionMapper.toEntity(requestFilter);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<CommentReaction> spec = CommentReactionSpecification.filterBy(filter);

        Page<CommentReaction> commentReactions = commentReactionRepository.findAll(spec, pageable);

        if (commentReactions.isEmpty()) {
            throw new CustomException(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND);
        }

        log.debug("Found {} comment reactions", commentReactions.getTotalElements());
        return commentReactionMapper.toDto(commentReactions);
    }

    @Override
    public CommentReactionDto update(Long id, CommentReactionRequest request) {
        log.debug("Updating comment reaction with ID: {} and request: {}", id, request);
        CommentReaction commentReaction = commentReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        Reaction reaction = reactionRepository
                .findById(request.reactionId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        commentReaction.setReaction(reaction);
        log.debug("Comment reaction updated successfully with ID: {}", id);

        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public CommentReactionDto delete(Long id) {
        log.debug("Deleting comment reaction with ID: {}", id);
        CommentReaction commentReaction = commentReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_REACTION_NOT_FOUND));

        commentReactionRepository.delete(commentReaction);
        log.debug("Post deleted successfully with ID: {}", id);

        return commentReactionMapper.toDto(commentReaction);
    }
}

