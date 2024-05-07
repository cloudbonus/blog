package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentDtoFilter;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentFilter;
import com.github.blog.service.mapper.CommentMapper;
import com.github.blog.service.mapper.PageableMapper;
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
public class CommentServiceImplTests {
    @Mock
    private CommentDao commentDao;
    @Mock
    private PageableMapper pageableMapper;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentRequest request;
    private CommentDto returnedCommentDto;
    private Comment comment;

    private final Long id = 1L;
    private final String content = "test content";

    @BeforeEach
    void setUp() {
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();
        String password = "test password";
        String email = "temp@test.temp";
        String title = "test title";
        String login = "test login";

        User user = new User();
        user.setId(id);
        user.setUsername(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        Post post = new Post();
        post.setUser(user);
        post.setId(id);
        post.setContent(content);
        post.setTitle(title);
        post.setPublishedAt(createdAt);

        request = new CommentRequest();
        request.setUserId(id);
        request.setPostId(id);
        request.setContent(content);

        comment = new Comment();
        comment.setId(id);
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setPublishedAt(createdAt);

        returnedCommentDto = new CommentDto();
        returnedCommentDto.setUserId(id);
        returnedCommentDto.setPostId(id);
        returnedCommentDto.setContent(content);
        returnedCommentDto.setPublishedAt(createdAt);
        returnedCommentDto.setId(id);
    }

    @Test
    @DisplayName("comment service: create")
    void create_returnsCommentDto_whenDataIsValid() {
        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(commentDao.create(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        CommentDto createdCommentDto = commentService.create(request);

        assertThat(createdCommentDto).isNotNull();
        assertThat(createdCommentDto.getId()).isEqualTo(id);
        assertThat(createdCommentDto.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        Optional<Comment> optionalComment = Optional.of(comment);

        when(commentDao.findById(id)).thenReturn(optionalComment);
        when(commentMapper.partialUpdate(request, comment)).thenReturn(comment);
        when(commentDao.update(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        CommentDto updatedCommentDto = commentService.update(id, request);

        assertThat(updatedCommentDto).isNotNull();
        assertThat(updatedCommentDto.getId()).isEqualTo(id);
        assertThat(updatedCommentDto.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: delete")
    void delete_deletesComment_whenDataIsValid() {
        Optional<Comment> optionalComment = Optional.of(comment);

        when(commentDao.findById(id)).thenReturn(optionalComment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        CommentDto deletedCommentDto = commentService.delete(id);

        assertThat(deletedCommentDto).isNotNull();
        assertThat(deletedCommentDto.getId()).isEqualTo(id);
        assertThat(deletedCommentDto.getContent()).isEqualTo(content);
        verify(commentDao, times(1)).delete(comment);
    }

    @Test
    @DisplayName("comment service: findAllByLogin")
    void find_findsAllCommentsByLogin_whenDataIsValid() {
        CommentFilter dtoFilter = new CommentFilter();
        dtoFilter.setLogin("test login");

        PageableRequest pageableRequest = new PageableRequest();

        Pageable pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);

        Page<Comment> comments = new Page<>(List.of(comment), pageable, 1L);

        CommentDtoFilter requestFilter = new CommentDtoFilter();
        requestFilter.setLogin("test login");

        when(commentMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(commentDao.findAll(dtoFilter, pageable)).thenReturn(comments);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        Page<CommentDto> filterSearchResult = commentService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(CommentDto::getContent).containsExactly(content);
        assertThat(filterSearchResult.getContent()).extracting(CommentDto::getId).containsExactly(id);
    }
}
