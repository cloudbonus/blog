package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.CommentRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.CommentFilter;
import com.github.blog.service.impl.CommentServiceImpl;
import com.github.blog.service.mapper.CommentMapper;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserAccessHandler userAccessHandler;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto returnedCommentDto;
    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final CommentRequest request = new CommentRequest(null, null);
    private final Comment comment = new Comment();

    private final Long id = 1L;
    private final String content = "test content";

    @BeforeEach
    void setUp() {
        returnedCommentDto = new CommentDto(id, null, null, content, null);
    }

    @Test
    @DisplayName("comment service: create")
    void create_returnsCommentDto_whenDataIsValid() {
        User user = new User();
        Post post = new Post();

        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(userAccessHandler.getUserId()).thenReturn(id);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        when(userRepository.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(postRepository.findById(request.postId())).thenReturn(Optional.of(post));

        CommentDto createdCommentDto = commentService.create(request);

        assertThat(createdCommentDto).isNotNull();
        assertThat(createdCommentDto.id()).isEqualTo(id);
        assertThat(createdCommentDto.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.partialUpdate(request, comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        CommentDto updatedCommentDto = commentService.update(id, request);

        assertThat(updatedCommentDto).isNotNull();
        assertThat(updatedCommentDto.id()).isEqualTo(id);
        assertThat(updatedCommentDto.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: delete")
    void delete_deletesComment_whenDataIsValid() {
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        CommentDto deletedCommentDto = commentService.delete(id);

        assertThat(deletedCommentDto).isNotNull();
        assertThat(deletedCommentDto.id()).isEqualTo(id);
        assertThat(deletedCommentDto.content()).isEqualTo(content);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("comment service: find all by username")
    void find_findsAllCommentsByUsername_whenDataIsValid() {
        CommentFilter filter = new CommentFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());

        Page<Comment> page = new PageImpl<>(List.of(comment), pageable, 1L);
        PageResponse<CommentDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedCommentDto), 1, 1, 0, 1);

        when(commentMapper.toEntity(any(CommentFilterRequest.class))).thenReturn(filter);
        when(commentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(commentMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<CommentDto> filterSearchResult = commentService.findAll(new CommentFilterRequest(null), new PageableRequest(null, null, null));

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(CommentDto::content).containsExactly(content);
        assertThat(filterSearchResult.content()).extracting(CommentDto::id).containsExactly(id);
    }
}
