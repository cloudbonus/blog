package com.github.blog.service;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Reaction;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.ReactionServiceImpl;
import com.github.blog.service.mapper.ReactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class ReactionServiceImplTests {

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private ReactionMapper reactionMapper;

    @InjectMocks
    private ReactionServiceImpl reactionService;

    private ReactionRequest request;
    private ReactionDto returnedReactionDto;
    private Reaction reaction;

    private final Long id = 1L;
    private final String reactionName = "LIKE";

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");

    @BeforeEach
    void setUp() {
        request = new ReactionRequest(reactionName);

        reaction = new Reaction();
        reaction.setId(id);
        reaction.setName(reactionName);

        returnedReactionDto = new ReactionDto(id, reactionName);
    }

    @Test
    @DisplayName("reaction service: create")
    void create_returnsReactionDto_whenDataIsValid() {
        when(reactionMapper.toEntity(request)).thenReturn(reaction);
        when(reactionRepository.save(reaction)).thenReturn(reaction);
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto createdReactionDto = reactionService.create(request);

        assertNotNull(createdReactionDto);
        assertEquals(id, createdReactionDto.id());
        assertEquals(reactionName, createdReactionDto.name());
    }

    @Test
    @DisplayName("reaction service: find by id")
    void findById_returnsReactionDto_whenIdIsValid() {
        when(reactionRepository.findById(id)).thenReturn(Optional.of(reaction));
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto foundReactionDto = reactionService.findById(id);

        assertNotNull(foundReactionDto);
        assertEquals(id, foundReactionDto.id());
        assertEquals(reactionName, foundReactionDto.name());
    }

    @Test
    @DisplayName("reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(reactionRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> reactionService.findById(id));

        assertEquals(ExceptionEnum.REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Reaction> page = new PageImpl<>(Collections.singletonList(reaction), pageable, 1L);
        PageResponse<ReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedReactionDto), 1, 1, 0, 1);

        when(reactionRepository.findAll(pageable)).thenReturn(page);
        when(reactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<ReactionDto> foundReactions = reactionService.findAll(pageableRequest);

        assertNotNull(foundReactions);
        assertEquals(1, foundReactions.content().size());
        assertEquals(id, foundReactions.content().get(0).id());
    }

    @Test
    @DisplayName("reaction service: find all - not found exception")
    void findAll_throwsException_whenNoReactionsFound() {
        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Reaction> page = new PageImpl<>(Collections.emptyList(), pageable, 1L);

        when(reactionRepository.findAll(pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> reactionService.findAll(pageableRequest));

        assertEquals(ExceptionEnum.REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("reaction service: update")
    void update_returnsUpdatedReactionDto_whenDataIsValid() {
        when(reactionRepository.findById(id)).thenReturn(Optional.of(reaction));
        when(reactionMapper.partialUpdate(request, reaction)).thenReturn(reaction);
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto updatedReactionDto = reactionService.update(id, request);

        assertNotNull(updatedReactionDto);
        assertEquals(id, updatedReactionDto.id());
        assertEquals(reactionName, updatedReactionDto.name());
    }

    @Test
    @DisplayName("reaction service: delete")
    void delete_returnsDeletedReactionDto_whenIdIsValid() {
        when(reactionRepository.findById(id)).thenReturn(Optional.of(reaction));
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto deletedReactionDto = reactionService.delete(id);

        assertNotNull(deletedReactionDto);
        assertEquals(id, deletedReactionDto.id());
        assertEquals(reactionName, deletedReactionDto.name());
        verify(reactionRepository, times(1)).delete(reaction);
    }
}
