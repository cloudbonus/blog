package com.github.blog.service;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Post;
import com.github.blog.model.PostReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.PostReactionRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.ReactionRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.PostReactionFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.PostReactionServiceImpl;
import com.github.blog.service.mapper.PostReactionMapper;
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
public class PostReactionServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostReactionRepository postReactionRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostReactionMapper postReactionMapper;

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

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final PostReactionFilterRequest postReactionFilterRequest = new PostReactionFilterRequest(null, null, null);

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
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(reactionRepository.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(userAccessHandler.getUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postReactionRepository.save(postReaction)).thenReturn(postReaction);
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto createdPostReactionDto = postReactionService.create(request);

        assertNotNull(createdPostReactionDto);
        assertEquals(id, createdPostReactionDto.id());
        verify(postReactionRepository, times(1)).save(postReaction);
    }

    @Test
    @DisplayName("post reaction service: find by id")
    void findById_returnsPostReactionDto_whenIdIsValid() {
        when(postReactionRepository.findById(id)).thenReturn(Optional.of(postReaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto foundPostReactionDto = postReactionService.findById(id);

        assertNotNull(foundPostReactionDto);
        assertEquals(id, foundPostReactionDto.id());
    }

    @Test
    @DisplayName("post reaction service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(postReactionRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> postReactionService.findById(id));

        assertEquals(ExceptionEnum.POST_REACTION_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("post reaction service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        PostReactionFilter filter = new PostReactionFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<PostReaction> page = new PageImpl<>(Collections.singletonList(postReaction), pageable, 1L);
        PageResponse<PostReactionDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedPostReactionDto), 1, 1, 0, 1);

        when(postReactionMapper.toEntity(any(PostReactionFilterRequest.class))).thenReturn(filter);
        when(postReactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(postReactionMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostReactionDto> foundPostReactions = postReactionService.findAll(postReactionFilterRequest, pageableRequest);

        assertNotNull(foundPostReactions);
        assertEquals(1, foundPostReactions.content().size());
        assertEquals(id, foundPostReactions.content().get(0).id());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("post reaction service: find all - not found exception")
    void findAll_throwsException_whenNoPostReactionsFound() {
        PostReactionFilter filter = new PostReactionFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<PostReaction> page = new PageImpl<>(Collections.emptyList(), pageable, 1L);

        when(postReactionMapper.toEntity(any(PostReactionFilterRequest.class))).thenReturn(filter);
        when(postReactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> postReactionService.findAll(postReactionFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.POST_REACTIONS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("post reaction service: update")
    void update_updatesPostReaction_whenDataIsValid() {
        when(postReactionRepository.findById(id)).thenReturn(Optional.of(postReaction));
        when(reactionRepository.findById(reactionId)).thenReturn(Optional.of(reaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto updatedPostReactionDto = postReactionService.update(id, request);

        assertNotNull(updatedPostReactionDto);
        assertEquals(id, updatedPostReactionDto.id());
    }

    @Test
    @DisplayName("post reaction service: delete")
    void delete_deletesPostReaction_whenIdIsValid() {
        when(postReactionRepository.findById(id)).thenReturn(Optional.of(postReaction));
        when(postReactionMapper.toDto(postReaction)).thenReturn(returnedPostReactionDto);

        PostReactionDto deletedPostReactionDto = postReactionService.delete(id);

        assertNotNull(deletedPostReactionDto);
        assertEquals(id, deletedPostReactionDto.id());
        verify(postReactionRepository, times(1)).delete(postReaction);
    }
}
