package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.CommentReactionRepository;
import com.github.blog.repository.CommentRepository;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.CommentReactionFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.CommentReactionServiceImpl;
import com.github.blog.service.mapper.CommentReactionMapper;
import com.github.blog.service.util.UserAccessHandler;
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
import org.springframework.data.jpa.domain.Specification;

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
public class CommentReactionServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentReactionRepository commentReactionRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private CommentReactionMapper commentReactionMapper;

    @Mock
    private UserAccessHandler userAccessHandler;

    @InjectMocks
    private CommentReactionServiceImpl commentReactionService;

    private CommentReactionRequest request;
    private CommentReactionDto returnedCommentReactionDto;
    private CommentReaction commentReaction;
    private Comment comment;
    private Reaction reaction;
    private final User user = new User();

    private final Long id = 1L;
    private final Long commentId = 1L;
    private final Long reactionId = 1L;

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final CommentReactionFilterRequest commentReactionFilterRequest = new CommentReactionFilterRequest(null, null, null);

    @BeforeEach
    void setUp() {
        request = new CommentReactionRequest(commentId, reactionId);

        comment = new Comment();
        comment.setId(commentId);

        reaction = new Reaction();
        reaction.setId(reactionId);

        commentReaction = new CommentReaction();
        commentReaction.setId(id);
        commentReaction.setComment(comment);
        commentReaction.setReaction(reaction);

        returnedCommentReactionDto = new CommentReactionDto(id, null, null, null);
    }

    @Test
    @DisplayName("comment reaction service: create")
    void create_createsCommentReaction_whenDataIsValid() {
        Long userId = 1L;

        when(commentReactionMapper.toEntity(request)).thenReturn(commentReaction);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(reactionRepository.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(userAccessHandler.getUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentReactionRepository.save(commentReaction)).thenReturn(commentReaction);
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto createdCommentReactionDto = commentReactionService.create(request);

        assertNotNull(createdCommentReactionDto);
        assertEquals(id, createdCommentReactionDto.id());
        verify(commentReactionRepository, times(1)).save(commentReaction);
    }

    @Test
    @DisplayName("comment reaction service: find by id")
    void findById_returnsCommentReactionDto_whenIdIsValid() {
        when(commentReactionRepository.findById(id)).thenReturn(Optional.of(commentReaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto foundCommentReactionDto = commentReactionService.findById(id);

        assertNotNull(foundCommentReactionDto);
        assertEquals(id, foundCommentReactionDto.id());
    }

    @Test
    @DisplayName("comment reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(commentReactionRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> commentReactionService.findById(id));

        assertEquals(ExceptionEnum.COMMENT_REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("comment reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        CommentReactionFilter filter = new CommentReactionFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<CommentReaction> page = new PageImpl<>(Collections.singletonList(commentReaction), pageable, 0);
        PageResponse<CommentReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedCommentReactionDto), 1, 1, 0, 1);

        when(commentReactionMapper.toEntity(any(CommentReactionFilterRequest.class))).thenReturn(filter);
        when(commentReactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(commentReactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<CommentReactionDto> foundCommentReactions = commentReactionService.findAll(commentReactionFilterRequest, pageableRequest);

        assertNotNull(foundCommentReactions);
        assertEquals(1, foundCommentReactions.content().size());
        assertEquals(id, foundCommentReactions.content().get(0).id());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("comment reaction service: find all - not found exception")
    void findAll_throwsException_whenNoCommentReactionsFound() {
        CommentReactionFilter filter = new CommentReactionFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<CommentReaction> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(commentReactionMapper.toEntity(any(CommentReactionFilterRequest.class))).thenReturn(filter);
        when(commentReactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> commentReactionService.findAll(commentReactionFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("comment reaction service: update")
    void update_updatesCommentReaction_whenDataIsValid() {
        when(commentReactionRepository.findById(id)).thenReturn(Optional.of(commentReaction));
        when(reactionRepository.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto updatedCommentReactionDto = commentReactionService.update(id, request);

        assertNotNull(updatedCommentReactionDto);
        assertEquals(id, updatedCommentReactionDto.id());
    }

    @Test
    @DisplayName("comment reaction service: delete")
    void delete_deletesCommentReaction_whenIdIsValid() {
        when(commentReactionRepository.findById(id)).thenReturn(Optional.of(commentReaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto deletedCommentReactionDto = commentReactionService.delete(id);

        assertNotNull(deletedCommentReactionDto);
        assertEquals(id, deletedCommentReactionDto.id());
        verify(commentReactionRepository, times(1)).delete(commentReaction);
    }
}
