package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.CommentReactionDao;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.CommentReactionServiceImpl;
import com.github.blog.service.mapper.CommentReactionMapper;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.util.UserAccessHandler;
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
public class CommentReactionServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private CommentReactionDao commentReactionDao;

    @Mock
    private CommentDao commentDao;

    @Mock
    private ReactionDao reactionDao;

    @Mock
    private CommentReactionMapper commentReactionMapper;

    @Mock
    private PageableMapper pageableMapper;

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

    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final Pageable pageable = new Pageable();
    private final CommentReactionFilterRequest commentReactionFilterRequest = new CommentReactionFilterRequest(null, null, null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

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
        when(commentDao.findById(commentId)).thenReturn(Optional.of(comment));
        when(reactionDao.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(userAccessHandler.getUserId()).thenReturn(userId);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(commentReactionDao.create(commentReaction)).thenReturn(commentReaction);
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto createdCommentReactionDto = commentReactionService.create(request);

        assertNotNull(createdCommentReactionDto);
        assertEquals(id, createdCommentReactionDto.id());
        verify(commentReactionDao, times(1)).create(commentReaction);
    }

    @Test
    @DisplayName("comment reaction service: find by id")
    void findById_returnsCommentReactionDto_whenIdIsValid() {
        when(commentReactionDao.findById(id)).thenReturn(Optional.of(commentReaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto foundCommentReactionDto = commentReactionService.findById(id);

        assertNotNull(foundCommentReactionDto);
        assertEquals(id, foundCommentReactionDto.id());
    }

    @Test
    @DisplayName("comment reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(commentReactionDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> commentReactionService.findById(id));

        assertEquals(ExceptionEnum.COMMENT_REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("comment reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        CommentReactionFilter filter = new CommentReactionFilter();

        Page<CommentReaction> page = new Page<>(Collections.singletonList(commentReaction), pageable, 1L);
        PageResponse<CommentReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedCommentReactionDto), pageableResponse, 1L);

        when(commentReactionMapper.toEntity(any(CommentReactionFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(commentReactionDao.findAll(filter, pageable)).thenReturn(page);
        when(commentReactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<CommentReactionDto> foundCommentReactions = commentReactionService.findAll(commentReactionFilterRequest, pageableRequest);

        assertNotNull(foundCommentReactions);
        assertEquals(1, foundCommentReactions.content().size());
        assertEquals(id, foundCommentReactions.content().get(0).id());
    }

    @Test
    @DisplayName("comment reaction service: find all - not found exception")
    void findAll_throwsException_whenNoCommentReactionsFound() {
        CommentReactionFilter filter = new CommentReactionFilter();

        Page<CommentReaction> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(commentReactionMapper.toEntity(any(CommentReactionFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(commentReactionDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> commentReactionService.findAll(commentReactionFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.COMMENT_REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("comment reaction service: update")
    void update_updatesCommentReaction_whenDataIsValid() {
        when(commentReactionDao.findById(id)).thenReturn(Optional.of(commentReaction));
        when(reactionDao.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto updatedCommentReactionDto = commentReactionService.update(id, request);

        assertNotNull(updatedCommentReactionDto);
        assertEquals(id, updatedCommentReactionDto.id());
    }

    @Test
    @DisplayName("comment reaction service: delete")
    void delete_deletesCommentReaction_whenIdIsValid() {
        when(commentReactionDao.findById(id)).thenReturn(Optional.of(commentReaction));
        when(commentReactionMapper.toDto(commentReaction)).thenReturn(returnedCommentReactionDto);

        CommentReactionDto deletedCommentReactionDto = commentReactionService.delete(id);

        assertNotNull(deletedCommentReactionDto);
        assertEquals(id, deletedCommentReactionDto.id());
        verify(commentReactionDao, times(1)).delete(commentReaction);
    }
}
