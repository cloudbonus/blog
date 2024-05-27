package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
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
        PostReaction postReaction = postReactionMapper.toEntity(request);

        Post post = postDao
                .findById(postReaction.getPost().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(postReaction.getReaction().getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        postReaction.setReaction(reaction);
        postReaction.setPost(post);
        postReaction.setUser(user);

        postReaction = postReactionDao.create(postReaction);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto findById(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PageResponse<PostReactionDto> findAll(PostReactionFilterRequest filterRequest, PageableRequest pageableRequest) {
        PostReactionFilter dtoFilter = postReactionMapper.toDto(filterRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<PostReaction> postReactions = postReactionDao.findAll(dtoFilter, pageable);

        if (postReactions.isEmpty()) {
            throw new CustomException(ExceptionEnum.POST_REACTIONS_NOT_FOUND);
        }

        return postReactionMapper.toDto(postReactions);
    }

    @Override
    public PostReactionDto update(Long id, PostReactionRequest request) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(request.getReactionId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        postReaction.setReaction(reaction);
        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto delete(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        postReactionDao.delete(postReaction);

        return postReactionMapper.toDto(postReaction);
    }
}
