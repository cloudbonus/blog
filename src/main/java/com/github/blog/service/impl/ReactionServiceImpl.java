package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Reaction;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.service.ReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.ReactionException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionDao reactionDao;
    private final ReactionMapper reactionMapper;
    private final PageableMapper pageableMapper;

    @Override
    public ReactionDto create(ReactionRequest request) {
        Reaction reaction = reactionMapper.toEntity(request);

        return reactionMapper.toDto(reactionDao.create(reaction));
    }

    @Override
    public ReactionDto findById(Long id) {
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> new ReactionException(ExceptionEnum.REACTION_NOT_FOUND));

        return reactionMapper.toDto(reaction);
    }

    @Override
    public Page<ReactionDto> findAll(PageableRequest pageableRequest) {
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<Reaction> reactions = reactionDao.findAll(pageable);

        if (reactions.isEmpty()) {
            throw new ReactionException(ExceptionEnum.REACTIONS_NOT_FOUND);
        }

        return reactions.map(reactionMapper::toDto);
    }

    @Override
    public ReactionDto update(Long id, ReactionRequest request) {
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> new ReactionException(ExceptionEnum.REACTION_NOT_FOUND));

        reaction = reactionMapper.partialUpdate(request, reaction);

        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDto delete(Long id) {
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> new ReactionException(ExceptionEnum.REACTION_NOT_FOUND));

        reactionDao.delete(reaction);

        return reactionMapper.toDto(reaction);
    }
}
