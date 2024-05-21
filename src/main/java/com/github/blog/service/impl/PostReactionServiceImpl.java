package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Post;
import com.github.blog.model.PostReaction;
import com.github.blog.model.Reaction;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.PostReactionDao;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostReactionFilter;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.PostException;
import com.github.blog.service.exception.impl.PostReactionException;
import com.github.blog.service.exception.impl.ReactionException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostReactionMapper;
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
    private final PostReactionDao postReactionDao;
    private final PostReactionMapper postReactionMapper;
    private final PageableMapper pageableMapper;
    private final PostDao postDao;
    private final ReactionDao reactionDao;

    @Override
    public PostReactionDto create(PostReactionRequest request) {
        PostReaction postReaction = postReactionMapper.toEntity(request);

        Post post = postDao
                .findById(postReaction.getPost().getId())
                .orElseThrow(() -> new PostException(ExceptionEnum.POST_NOT_FOUND));

        Reaction reaction = reactionDao
                .findById(postReaction.getReaction().getId())
                .orElseThrow(() -> new ReactionException(ExceptionEnum.REACTION_NOT_FOUND));

        postReaction.setReaction(reaction);
        postReaction.setPost(post);

        postReaction = postReactionDao.create(postReaction);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto findById(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public Page<PostReactionDto> findAll(PostReactionDtoFilter filterRequest, PageableRequest pageableRequest) {
        PostReactionFilter dtoFilter = postReactionMapper.toDto(filterRequest);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<PostReaction> postReactions = postReactionDao.findAll(dtoFilter, pageable);

        if (postReactions.isEmpty()) {
            throw new PostReactionException(ExceptionEnum.POST_REACTIONS_NOT_FOUND);
        }

        return postReactions.map(postReactionMapper::toDto);
    }

    @Override
    public PostReactionDto update(Long id, PostReactionRequest request) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        postReaction = postReactionMapper.partialUpdate(request, postReaction);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto delete(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(ExceptionEnum.POST_REACTION_NOT_FOUND));

        postReactionDao.delete(postReaction);

        return postReactionMapper.toDto(postReaction);
    }
}
