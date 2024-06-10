package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Reaction;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.service.ReactionService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.ReactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    @Override
    public ReactionDto create(ReactionRequest request) {
        log.debug("Creating a new reaction with request: {}", request);
        Reaction reaction = reactionMapper.toEntity(request);

        reaction = reactionRepository.save(reaction);
        log.debug("Reaction created successfully with ID: {}", reaction.getId());
        return reactionMapper.toDto(reaction);
    }

    @Override
    @Transactional(readOnly = true)
    public ReactionDto findById(Long id) {
        log.debug("Finding reaction by ID: {}", id);
        Reaction reaction = reactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        log.debug("Reaction found with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReactionDto> findAll(PageableRequest pageableRequest) {
        log.debug("Finding all reactions with pageable: {}", pageableRequest);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());

        Page<Reaction> reactions = reactionRepository.findAll(pageable);

        if (reactions.isEmpty()) {
            throw new CustomException(ExceptionEnum.REACTIONS_NOT_FOUND);
        }

        log.debug("Found {} reactions", reactions.getTotalElements());
        return reactionMapper.toDto(reactions);
    }

    @Override
    public ReactionDto update(Long id, ReactionRequest request) {
        log.debug("Updating reaction with ID: {} and request: {}", id, request);
        Reaction reaction = reactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        reaction = reactionMapper.partialUpdate(request, reaction);
        log.debug("Reaction updated successfully with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDto delete(Long id) {
        log.debug("Deleting reaction with ID: {}", id);
        Reaction reaction = reactionRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.REACTION_NOT_FOUND));

        reactionRepository.delete(reaction);
        log.debug("Reaction deleted successfully with ID: {}", id);
        return reactionMapper.toDto(reaction);
    }
}