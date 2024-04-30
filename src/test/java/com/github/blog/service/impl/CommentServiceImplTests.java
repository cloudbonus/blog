package com.github.blog.service.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.dto.common.CommentDto;
import com.github.blog.dto.filter.CommentDtoFilter;
import com.github.blog.dto.request.CommentRequestFilter;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.service.mapper.CommentMapper;
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
    CommentDao commentDao;
    @Mock
    CommentMapper commentMapper;
    @InjectMocks
    CommentServiceImpl commentService;

    private CommentDto commentDto;
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
        user.setLogin(login);
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

        commentDto = new CommentDto();
        commentDto.setUserId(id);
        commentDto.setPostId(id);
        commentDto.setContent(content);
        commentDto.setPublishedAt(createdAt);

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
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentDao.create(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        CommentDto createdCommentDto = commentService.create(commentDto);

        assertThat(createdCommentDto).isNotNull();
        assertThat(createdCommentDto.getId()).isEqualTo(id);
        assertThat(createdCommentDto.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        Optional<Comment> optionalComment = Optional.of(comment);

        when(commentDao.findById(id)).thenReturn(optionalComment);
        when(commentMapper.partialUpdate(commentDto, comment)).thenReturn(comment);
        when(commentDao.update(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        CommentDto updatedCommentDto = commentService.update(id, commentDto);

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
        CommentDtoFilter dtoFilter = new CommentDtoFilter();
        dtoFilter.setLogin("test login");

        List<Comment> comments = List.of(comment);

        CommentRequestFilter requestFilter = new CommentRequestFilter();
        requestFilter.setLogin("test login");

        when(commentMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(commentDao.findAll(dtoFilter)).thenReturn(comments);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        List<CommentDto> filterSearchResult = commentService.findAll(requestFilter);

        assertThat(filterSearchResult).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult).extracting(CommentDto::getContent).containsExactly(content);
        assertThat(filterSearchResult).extracting(CommentDto::getId).containsExactly(id);
    }
}
