package com.github.blog.service;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.service.impl.PostServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostMapper;
import com.github.blog.service.util.UserAccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private PostDao postDao;

    @Mock
    private UserDao userDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserAccessHandler userAccessHandler;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private PostDto returnedPostDto;
    private final PostRequest request = new PostRequest(null, null, null);
    private final Post post = new Post();

    private final Long id = 1L;
    private final String title = "test title";

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final PostFilterRequest postFilterRequest = new PostFilterRequest(null, null, null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

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
        when(userDao.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(userAccessHandler.hasRole("ROLE_COMPANY")).thenReturn(false);
        when(postDao.create(post)).thenReturn(post);
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
        when(userDao.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(userAccessHandler.hasRole("ROLE_COMPANY")).thenReturn(true);
        when(postDao.create(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto createdPostDto = postService.create(request);

        assertThat(createdPostDto).isNotNull();
        assertThat(createdPostDto.id()).isEqualTo(id);
        assertThat(createdPostDto.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("post service: update")
    void update_returnsUpdatedPostDto_whenDataIsValid() {
        when(postDao.findById(id)).thenReturn(Optional.of(post));
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
        when(postDao.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto deletedPostDto = postService.delete(id);

        assertThat(deletedPostDto).isNotNull();
        assertThat(deletedPostDto.id()).isEqualTo(id);
        assertThat(deletedPostDto.title()).isEqualTo(title);
        verify(postDao, times(1)).delete(post);
    }

    @Test
    @DisplayName("post service: find all by username")
    void find_findsAllPostsByUsername_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        Page<Post> page = new Page<>(List.of(post), pageable, 1L);
        PageResponse<PostDto> pageResponse = new PageResponse<>(List.of(returnedPostDto), pageableResponse, 1L);

        when(postMapper.toEntity(any(PostFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(postDao.findAll(filter, pageable)).thenReturn(page);
        when(postMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostDto> filterSearchResult = postService.findAll(postFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(PostDto::title).containsExactly(title);
        assertThat(filterSearchResult.content()).extracting(PostDto::id).containsExactly(id);
    }

    @Test
    @DisplayName("post service: find all by tag")
    void find_findsAllPostsByTag_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        Page<Post> page = new Page<>(List.of(post), pageable, 1L);
        PageResponse<PostDto> pageResponse = new PageResponse<>(List.of(returnedPostDto), pageableResponse, 1L);

        when(postMapper.toEntity(any(PostFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(postDao.findAll(filter, pageable)).thenReturn(page);
        when(postMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<PostDto> filterSearchResult = postService.findAll(postFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(PostDto::title).containsExactly(title);
        assertThat(filterSearchResult.content()).extracting(PostDto::id).containsExactly(id);
    }
}
