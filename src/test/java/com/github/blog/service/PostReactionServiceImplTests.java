package com.github.blog.service;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
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
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.PostReactionServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostReactionMapper;
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
public class PostReactionServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private PostReactionDao postReactionDao;

    @Mock
    private ReactionDao reactionDao;

    @Mock
    private PostDao postDao;

    @Mock
    private PostReactionMapper postReactionMapper;

    @Mock
    private PageableMapper pageableMapper;

    @Mock
    private UserAccessHandler userAccessHandler;

    @InjectMocks
    private PostReactionServiceImpl postReactionService;

    private PostReactionRequest request;
    private PostReactionDto returnedPostReactionDto;
    private PostReaction postReaction;
    private Post post;
    private Reaction reaction;
    private final User user = new User();

    private final Long id = 1L;
    private final Long postId = 1L;
    private final Long reactionId = 1L;

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final PostReactionFilterRequest postReactionFilterRequest = new PostReactionFilterRequest(null, null, null);

    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

    @BeforeEach
    void setUp() {
        request = new PostReactionRequest(postId, reactionId);

        post = new Post();
        post.setId(postId);

        reaction = new Reaction();
        reaction.setId(reactionId);

        postReaction = new PostReaction();
        postReaction.setId(id);
        postReaction.setReaction(reaction);
        postReaction.setPost(post);

        returnedPostReactionDto = new PostReactionDto(id, null, null, null);
    }

    @Test
    @DisplayName("post reaction service: create")
    void create_createsPostReaction_whenDataIsValid() {
        Long userId = 1L;

        when(postReactionMapper.toEntity(request)).thenReturn(postReaction);
        when(postDao.findById(postId)).thenReturn(Optional.of(post));
        when(reactionDao.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(userAccessHandler.getUserId()).thenReturn(userId);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(postReactionDao.create(postReaction)).thenReturn(postReaction);
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto createdPostReactionDto = postReactionService.create(request);

        assertNotNull(createdPostReactionDto);
        assertEquals(id, createdPostReactionDto.id());
        verify(postReactionDao, times(1)).create(postReaction);
    }

    @Test
    @DisplayName("post reaction service: find by id")
    void findById_returnsPostReactionDto_whenIdIsValid() {
        when(postReactionDao.findById(id)).thenReturn(Optional.of(postReaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto foundPostReactionDto = postReactionService.findById(id);

        assertNotNull(foundPostReactionDto);
        assertEquals(id, foundPostReactionDto.id());
    }

    @Test
    @DisplayName("post reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(postReactionDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> postReactionService.findById(id));

        assertEquals(ExceptionEnum.POST_REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("post reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        PostReactionFilter filter = new PostReactionFilter();

        Page<PostReaction> page = new Page<>(Collections.singletonList(postReaction), pageable, 1L);
        PageResponse<PostReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedPostReactionDto), pageableResponse, 1L);

        when(postReactionMapper.toEntity(any(PostReactionFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(postReactionDao.findAll(filter, pageable)).thenReturn(page);
        when(postReactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostReactionDto> foundPostReactions = postReactionService.findAll(postReactionFilterRequest, pageableRequest);

        assertNotNull(foundPostReactions);
        assertEquals(1, foundPostReactions.content().size());
        assertEquals(id, foundPostReactions.content().get(0).id());
    }

    @Test
    @DisplayName("post reaction service: find all - not found exception")
    void findAll_throwsException_whenNoPostReactionsFound() {
        PostReactionFilter filter = new PostReactionFilter();

        Page<PostReaction> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(postReactionMapper.toEntity(any(PostReactionFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(postReactionDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> postReactionService.findAll(postReactionFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.POST_REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("post reaction service: update")
    void update_updatesPostReaction_whenDataIsValid() {
        when(postReactionDao.findById(id)).thenReturn(Optional.of(postReaction));
        when(reactionDao.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto updatedPostReactionDto = postReactionService.update(id, request);

        assertNotNull(updatedPostReactionDto);
        assertEquals(id, updatedPostReactionDto.id());
    }

    @Test
    @DisplayName("post reaction service: delete")
    void delete_deletesPostReaction_whenIdIsValid() {
        when(postReactionDao.findById(id)).thenReturn(Optional.of(postReaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto deletedPostReactionDto = postReactionService.delete(id);

        assertNotNull(deletedPostReactionDto);
        assertEquals(id, deletedPostReactionDto.id());
        verify(postReactionDao, times(1)).delete(postReaction);
    }
}
