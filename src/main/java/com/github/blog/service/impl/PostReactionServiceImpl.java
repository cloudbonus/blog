package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Post;
import com.github.blog.model.PostReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.PostReactionDao;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostReactionFilter;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostReactionMapper;
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
public class PostReactionServiceImpl implements PostReactionService {
    private final UserDao userDao;
    private final PostReactionDao postReactionDao;
    private final ReactionDao reactionDao;
    private final PostDao postDao;

    private final PostReactionMapper postReactionMapper;
    private final PageableMapper pageableMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public PostReactionDto create(PostReactionRequest request) {
        log.debug("Creating a new post reaction with request: {}", request);
        PostReaction postReaction = postReactionMapper.toEntity(request);

        Post post = postDao
                .findById(postReaction.getPost().getId())
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", request.getPostId());
                    return new CustomException(ExceptionEnum.POST_NOT_FOUND);
                });

        Reaction reaction = reactionDao
                .findById(postReaction.getReaction().getId())
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

        postReaction.setReaction(reaction);
        postReaction.setPost(post);
        postReaction.setUser(user);

        postReaction = postReactionDao.create(postReaction);
        log.info("Post reaction created successfully with ID: {}", post.getId());

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto findById(Long id) {
        log.debug("Finding post reaction by ID: {}", id);
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Post Reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND);
                });

        log.debug("Post reaction found with ID: {}", id);
        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PageResponse<PostReactionDto> findAll(PostReactionFilterRequest filterRequest, PageableRequest pageableRequest) {
        log.debug("Finding all post reactions with filter: {} and pageable: {}", filterRequest, pageableRequest);
        PostReactionFilter filter = postReactionMapper.toEntity(filterRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<PostReaction> postReactions = postReactionDao.findAll(filter, pageable);

        if (postReactions.isEmpty()) {
            log.error("No post reactions found with the given filter and pageable");
            throw new CustomException(ExceptionEnum.POST_REACTIONS_NOT_FOUND);
        }

        log.info("Found {} post reactions", postReactions.getTotalNumberOfEntities());
        return postReactionMapper.toDto(postReactions);
    }

    @Override
    public PostReactionDto update(Long id, PostReactionRequest request) {
        log.debug("Updating post reaction with ID: {} and request: {}", id, request);
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("PostReaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND);
                });

        Reaction reaction = reactionDao
                .findById(request.getReactionId())
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", request.getReactionId());
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        postReaction.setReaction(reaction);
        log.debug("Post reaction updated successfully with ID: {}", id);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto delete(Long id) {
        log.debug("Deleting post reaction with ID: {}", id);
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Post reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND);
                });

        postReactionDao.delete(postReaction);
        log.debug("Post reaction deleted successfully with ID: {}", id);

        return postReactionMapper.toDto(postReaction);
    }
}