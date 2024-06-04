package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Reaction;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.service.ReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.ReactionMapper;
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
public class ReactionServiceImpl implements ReactionService {
    private final ReactionDao reactionDao;

    private final ReactionMapper reactionMapper;
    private final PageableMapper pageableMapper;

    @Override
    public ReactionDto create(ReactionRequest request) {
        log.info("Creating a new reaction with request: {}", request);
        Reaction reaction = reactionMapper.toEntity(request);

        reaction = reactionDao.create(reaction);
        log.info("Reaction created successfully with ID: {}", reaction.getId());
        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDto findById(Long id) {
        log.info("Finding reaction by ID: {}", id);
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        log.info("Reaction found with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }

    @Override
    public PageResponse<ReactionDto> findAll(PageableRequest pageableRequest) {
        log.info("Finding all reactions with pageable: {}", pageableRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Reaction> reactions = reactionDao.findAll(pageable);

        if (reactions.isEmpty()) {
            log.error("No reactions found with the given pageable");
            throw new CustomException(ExceptionEnum.REACTIONS_NOT_FOUND);
        }

        log.info("Found {} reactions", reactions.getTotalNumberOfEntities());
        return reactionMapper.toDto(reactions);
    }

    @Override
    public ReactionDto update(Long id, ReactionRequest request) {
        log.info("Updating reaction with ID: {} and request: {}", id, request);
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        reaction = reactionMapper.partialUpdate(request, reaction);
        log.info("Reaction updated successfully with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDto delete(Long id) {
        log.info("Deleting reaction with ID: {}", id);
        Reaction reaction = reactionDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Reaction not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.REACTION_NOT_FOUND);
                });

        reactionDao.delete(reaction);
        log.info("Reaction deleted successfully with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }
}