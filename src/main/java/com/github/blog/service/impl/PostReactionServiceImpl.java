package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.model.PostReaction;
import com.github.blog.repository.PostReactionDao;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.exception.PostReactionErrorResult;
import com.github.blog.service.exception.impl.PostReactionException;
import com.github.blog.service.mapper.PostReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {

    private final PostReactionDao postReactionDao;
    private final PostReactionMapper postReactionMapper;

    @Override
    public PostReactionDto create(PostReactionDto postReactionDto) {
        PostReaction postReaction = postReactionMapper.toEntity(postReactionDto);
        return postReactionMapper.toDto(postReactionDao.create(postReaction));
    }

    @Override
    public PostReactionDto findById(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(PostReactionErrorResult.POST_REACTION_NOT_FOUND));

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public List<PostReactionDto> findAll() {
        List<PostReaction> postReactions = postReactionDao.findAll();

        if (postReactions.isEmpty()) {
            throw new PostReactionException(PostReactionErrorResult.POST_REACTIONS_NOT_FOUND);
        }

        return postReactions.stream().map(postReactionMapper::toDto).toList();
    }

    @Override
    public PostReactionDto update(Long id, PostReactionDto postReactionDto) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(PostReactionErrorResult.POST_REACTION_NOT_FOUND));

        postReaction = postReactionMapper.partialUpdate(postReactionDto, postReaction);
        postReaction = postReactionDao.update(postReaction);

        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public PostReactionDto delete(Long id) {
        PostReaction postReaction = postReactionDao
                .findById(id)
                .orElseThrow(() -> new PostReactionException(PostReactionErrorResult.POST_REACTION_NOT_FOUND));
        postReactionDao.delete(postReaction);
        return postReactionMapper.toDto(postReaction);
    }
}
