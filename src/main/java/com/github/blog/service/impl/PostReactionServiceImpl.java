package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.PostReactionRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.entity.Post;
import com.github.blog.repository.entity.PostReaction;
import com.github.blog.repository.entity.Reaction;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.filter.PostReactionFilter;
import com.github.blog.repository.specification.PostReactionSpecification;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PostReactionMapper;
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
public class PostReactionServiceImpl implements PostReactionService {
    private final UserRepository userRepository;
    private final PostReactionRepository postReactionRepository;
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    private final PostReactionMapper postReactionMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public PostReactionDto create(PostReactionRequest request) {
        log.debug("Creating a new post reaction with request: {}", request);
        PostReaction postReaction = postReactionMapper.toEntity(request);

        Post post = postRepository
                .findById(postReaction.getPost().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        Reaction reaction = reactionRepository
                .findById(postReaction.getReaction().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        User user = userRepository
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        postReaction.setReaction(reaction);
        postReaction.setPost(post);
        postReaction.setUser(user);

        postReaction = postReactionRepository.save(postReaction);
        log.debug("Post reaction created successfully with ID: {}", post.getId());

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PostReactionDto findById(Long id) {
        log.debug("Finding post reaction by ID: {}", id);
        PostReaction postReaction = postReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        log.debug("Post reaction found with ID: {}", id);
        return postReactionMapper.toDto(postReaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostReactionDto> findAll(PostReactionFilterRequest filterRequest, PageableRequest pageableRequest) {
        log.debug("Finding all post reactions with filter: {} and pageable: {}", filterRequest, pageableRequest);
        PostReactionFilter filter = postReactionMapper.toEntity(filterRequest);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<PostReaction> spec = PostReactionSpecification.filterBy(filter);
        Page<PostReaction> postReactions = postReactionRepository.findAll(spec, pageable);

        if (postReactions.isEmpty()) {
            throw new CustomException(ExceptionEnum.POST_REACTIONS_NOT_FOUND);
        }

        log.debug("Found {} post reactions", postReactions.getTotalElements());
        return postReactionMapper.toDto(postReactions);
    }

    @Override
    public PostReactionDto update(Long id, PostReactionRequest request) {
        log.debug("Updating post reaction with ID: {} and request: {}", id, request);
        PostReaction postReaction = postReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        Reaction reaction = reactionRepository
                .findById(request.reactionId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        postReaction.setReaction(reaction);
        log.debug("Post reaction updated successfully with ID: {}", id);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto delete(Long id) {
        log.debug("Deleting post reaction with ID: {}", id);
        PostReaction postReaction = postReactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        postReactionRepository.delete(postReaction);
        log.debug("Post reaction deleted successfully with ID: {}", id);

        return postReactionMapper.toDto(postReaction);
    }
}