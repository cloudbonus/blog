package com.github.blog.service;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Reaction;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.ReactionServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.ReactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class ReactionServiceImplTests {

    @Mock
    private ReactionDao reactionDao;

    @Mock
    private ReactionMapper reactionMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private ReactionServiceImpl reactionService;

    private ReactionRequest request;
    private ReactionDto returnedReactionDto;
    private Reaction reaction;

    private final Long id = 1L;
    private final String reactionName = "LIKE";

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

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
        when(reactionDao.create(reaction)).thenReturn(reaction);
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto createdReactionDto = reactionService.create(request);

        assertNotNull(createdReactionDto);
        assertEquals(id, createdReactionDto.id());
        assertEquals(reactionName, createdReactionDto.name());
    }

    @Test
    @DisplayName("reaction service: find by id")
    void findById_returnsReactionDto_whenIdIsValid() {
        when(reactionDao.findById(id)).thenReturn(Optional.of(reaction));
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto foundReactionDto = reactionService.findById(id);

        assertNotNull(foundReactionDto);
        assertEquals(id, foundReactionDto.id());
        assertEquals(reactionName, foundReactionDto.name());
    }

    @Test
    @DisplayName("reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(reactionDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> reactionService.findById(id));

        assertEquals(ExceptionEnum.REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        Page<Reaction> page = new Page<>(Collections.singletonList(reaction), pageable, 1L);
        PageResponse<ReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedReactionDto), pageableResponse, 1L);

        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(reactionDao.findAll(pageable)).thenReturn(page);
        when(reactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<ReactionDto> foundReactions = reactionService.findAll(pageableRequest);

        assertNotNull(foundReactions);
        assertEquals(1, foundReactions.content().size());
        assertEquals(id, foundReactions.content().get(0).id());
    }

    @Test
    @DisplayName("reaction service: find all - not found exception")
    void findAll_throwsException_whenNoReactionsFound() {
        Page<Reaction> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(reactionDao.findAll(pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> reactionService.findAll(pageableRequest));

        assertEquals(ExceptionEnum.REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("reaction service: update")
    void update_returnsUpdatedReactionDto_whenDataIsValid() {
        when(reactionDao.findById(id)).thenReturn(Optional.of(reaction));
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
        when(reactionDao.findById(id)).thenReturn(Optional.of(reaction));
        when(reactionMapper.toDto(reaction)).thenReturn(returnedReactionDto);

        ReactionDto deletedReactionDto = reactionService.delete(id);

        assertNotNull(deletedReactionDto);
        assertEquals(id, deletedReactionDto.id());
        assertEquals(reactionName, deletedReactionDto.name());
        verify(reactionDao, times(1)).delete(reaction);
    }
}
