package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostDtoFilter;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Post;
import com.github.blog.model.Tag;
import com.github.blog.model.User;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private PostMapper postMapper;
    @Mock
    private PageableMapper pageableMapper;
    @InjectMocks
    private PostServiceImpl postService;

    private PostRequest request;
    private PostDto returnedPostDto;
    private Post post;

    private final Long id = 1L;
    private final String title = "test title";

    private static Pageable pageable;
    private static PageableRequest pageableRequest;

    @BeforeAll
    public static void setUpPageable() {
        pageableRequest = new PageableRequest();

        pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
    }

    @BeforeEach
    void setUp() {
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();
        String password = "test password";
        String email = "temp@test.temp";
        String content = "test content";
        String login = "test login";

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        Tag tag = new Tag();
        tag.setTagName("news");
        tag.setId(id);

        request = new PostRequest();
        request.setUserId(id);
        request.setContent(content);
        request.setTitle(title);

        post = new Post();
        post.setUser(user);
        post.setId(id);
        post.setContent(content);
        post.setTitle(title);
        post.setPublishedAt(createdAt);
        post.getTags().add(tag);

        tag.getPosts().add(post);

        returnedPostDto = new PostDto();
        returnedPostDto.setUserId(id);
        returnedPostDto.setId(id);
        returnedPostDto.setContent(content);
        returnedPostDto.setTitle(title);
        returnedPostDto.setPublishedAt(createdAt);
    }


    @Test
    @DisplayName("post service: create")
    void create_returnsPostDto_whenDataIsValid() {
        when(postMapper.toEntity(request)).thenReturn(post);
        when(postDao.create(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto createdPostDto = postService.create(request);

        assertThat(createdPostDto).isNotNull();
        assertThat(createdPostDto.getId()).isEqualTo(id);
        assertThat(createdPostDto.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("post service: update")
    void update_returnsUpdatedPostDto_whenDataIsValid() {
        Optional<Post> optionalPost = Optional.of(post);

        when(postDao.findById(id)).thenReturn(optionalPost);
        when(postMapper.partialUpdate(request, post)).thenReturn(post);
        when(postDao.update(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto updatedPostDto = postService.update(id, request);

        assertThat(updatedPostDto).isNotNull();
        assertThat(updatedPostDto.getId()).isEqualTo(id);
        assertThat(updatedPostDto.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("comment service: delete")
    void delete_deletesComment_whenDataIsValid() {
        Optional<Post> optionalPost = Optional.of(post);

        when(postDao.findById(id)).thenReturn(optionalPost);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        PostDto deletedPostDto = postService.delete(id);

        assertThat(deletedPostDto).isNotNull();
        assertThat(deletedPostDto.getId()).isEqualTo(id);
        assertThat(deletedPostDto.getTitle()).isEqualTo(title);
        verify(postDao, times(1)).delete(post);
    }

    @Test
    @DisplayName("post service: findAllByLogin")
    void find_findsAllPostsByLogin_whenDataIsValid() {
        PostFilter dtoFilter = new PostFilter();
        dtoFilter.setLogin("test login");

        Page<Post> posts = new Page<>(List.of(post), pageable, 1L);

        PostDtoFilter requestFilter = new PostDtoFilter();
        requestFilter.setLogin("test login");

        when(postMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(postDao.findAll(dtoFilter, pageable)).thenReturn(posts);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        Page<PostDto> filterSearchResult = postService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(PostDto::getTitle).containsExactly(title);
        assertThat(filterSearchResult.getContent()).extracting(PostDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("post service: findAllByLogin")
    void find_findsAllPostsByTag_whenDataIsValid() {
        PostFilter dtoFilter = new PostFilter();
        dtoFilter.setTag("news");

        Page<Post> posts = new Page<>(List.of(post), pageable, 1L);

        PostDtoFilter requestFilter = new PostDtoFilter();
        requestFilter.setTag("news");

        when(postMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(postDao.findAll(dtoFilter, pageable)).thenReturn(posts);
        when(postMapper.toDto(post)).thenReturn(returnedPostDto);

        Page<PostDto> filterSearchResult = postService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(PostDto::getTitle).containsExactly(title);
        assertThat(filterSearchResult.getContent()).extracting(PostDto::getId).containsExactly(id);
    }
}
