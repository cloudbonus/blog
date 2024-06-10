package com.github.blog.service;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.OrderRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.PostFilter;
import com.github.blog.service.impl.PostServiceImpl;
import com.github.blog.service.mapper.PostMapper;
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
public class PostServiceImplTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserAccessHandler userAccessHandler;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private PostDto returnedPostDto;
    private final PostRequest request = new PostRequest(null, null, null);
    private final Post post = new Post();

    private final Long id = 1L;
    private final String title = "test title";

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final PostFilterRequest postFilterRequest = new PostFilterRequest(null, null, null);

    @BeforeEach
    void setUp() {
        returnedPostDto = new PostDto(id, null, title, null, null, null, null);
    }

    @Test
    @DisplayName("post service: create if student role")
    void create_returnsPostDto_whenDataIsValidAndRoleIsNotCompany() {
        User user = new User();

        when(postMapper.toEntity(request)).thenReturn(post);
        when(userAccessHandler.getUserId()).thenReturn(id);
        when(userRepository.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(userAccessHandler.hasRole("ROLE_COMPANY")).thenReturn(false);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto createdPostDto = postService.create(request);

        assertThat(createdPostDto).isNotNull();
        assertThat(createdPostDto.id()).isEqualTo(id);
        assertThat(createdPostDto.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("post service: create if company role")
    void create_returnsPostDto_whenDataIsValidAndRoleIsCompany() {
        User user = new User();

        when(postMapper.toEntity(request)).thenReturn(post);
        when(userAccessHandler.getUserId()).thenReturn(id);
        when(userRepository.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(userAccessHandler.hasRole("ROLE_COMPANY")).thenReturn(true);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto createdPostDto = postService.create(request);

        assertThat(createdPostDto).isNotNull();
        assertThat(createdPostDto.id()).isEqualTo(id);
        assertThat(createdPostDto.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("post service: update")
    void update_returnsUpdatedPostDto_whenDataIsValid() {
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.partialUpdate(request, post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto updatedPostDto = postService.update(id, request);

        assertThat(updatedPostDto).isNotNull();
        assertThat(updatedPostDto.id()).isEqualTo(id);
        assertThat(updatedPostDto.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("comment service: delete")
    void delete_deletesComment_whenDataIsValid() {
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto deletedPostDto = postService.delete(id);

        assertThat(deletedPostDto).isNotNull();
        assertThat(deletedPostDto.id()).isEqualTo(id);
        assertThat(deletedPostDto.title()).isEqualTo(title);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("post service: find all by username")
    void find_findsAllPostsByUsername_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Post> page = new PageImpl<>(List.of(post), pageable, 1L);
        PageResponse<PostDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedPostDto), 1, 1, 0, 1);

        when(postMapper.toEntity(any(PostFilterRequest.class))).thenReturn(filter);
        when(postRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(postMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostDto> filterSearchResult = postService.findAll(postFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(PostDto::title).containsExactly(title);
        assertThat(filterSearchResult.content()).extracting(PostDto::id).containsExactly(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("post service: find all by tag")
    void find_findsAllPostsByTag_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Post> page = new PageImpl<>(List.of(post), pageable, 1L);
        PageResponse<PostDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedPostDto), 1, 1, 0, 1);

        when(postMapper.toEntity(any(PostFilterRequest.class))).thenReturn(filter);
        when(postRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(postMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostDto> filterSearchResult = postService.findAll(postFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(PostDto::title).containsExactly(title);
        assertThat(filterSearchResult.content()).extracting(PostDto::id).containsExactly(id);
    }
}
