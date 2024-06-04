package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.CommentFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentFilter;
import com.github.blog.service.impl.CommentServiceImpl;
import com.github.blog.service.mapper.CommentMapper;
import com.github.blog.service.mapper.PageableMapper;
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
public class CommentServiceImplTests {

    @Mock
    private CommentDao commentDao;

    @Mock
    private UserDao userDao;

    @Mock
    private PostDao postDao;

    @Mock
    private PageableMapper pageableMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserAccessHandler userAccessHandler;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto returnedCommentDto;
    private final CommentRequest request = new CommentRequest();
    private final Comment comment = new Comment();

    private final Long id = 1L;
    private final String content = "test content";

    @BeforeEach
    void setUp() {
        returnedCommentDto = new CommentDto();
        returnedCommentDto.setContent(content);
        returnedCommentDto.setId(id);
    }

    @Test
    @DisplayName("comment service: create")
    void create_returnsCommentDto_whenDataIsValid() {
        User user = new User();
        Post post = new Post();

        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(userAccessHandler.getUserId()).thenReturn(id);
        when(commentDao.create(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        when(userDao.findById(userAccessHandler.getUserId())).thenReturn(Optional.of(user));
        when(postDao.findById(request.getPostId())).thenReturn(Optional.of(post));

        CommentDto createdCommentDto = commentService.create(request);

        assertThat(createdCommentDto).isNotNull();
        assertThat(createdCommentDto.getId()).isEqualTo(id);
        assertThat(createdCommentDto.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        when(commentDao.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.partialUpdate(request, comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);
        CommentDto updatedCommentDto = commentService.update(id, request);

        assertThat(updatedCommentDto).isNotNull();
        assertThat(updatedCommentDto.getId()).isEqualTo(id);
        assertThat(updatedCommentDto.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("comment service: delete")
    void delete_deletesComment_whenDataIsValid() {
        when(commentDao.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(returnedCommentDto);

        CommentDto deletedCommentDto = commentService.delete(id);

        assertThat(deletedCommentDto).isNotNull();
        assertThat(deletedCommentDto.getId()).isEqualTo(id);
        assertThat(deletedCommentDto.getContent()).isEqualTo(content);
        verify(commentDao, times(1)).delete(comment);
    }

    @Test
    @DisplayName("comment service: find all by username")
    void find_findsAllCommentsByUsername_whenDataIsValid() {
        CommentFilter filter = new CommentFilter();

        Pageable pageable = new Pageable();
        PageableResponse pageableResponse = new PageableResponse();

        Page<Comment> page = new Page<>(List.of(comment), pageable, 1L);
        PageResponse<CommentDto> pageResponse = new PageResponse<>(List.of(returnedCommentDto), pageableResponse, 1L);

        when(commentMapper.toEntity(any(CommentFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(commentDao.findAll(filter, pageable)).thenReturn(page);
        when(commentMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<CommentDto> filterSearchResult = commentService.findAll(new CommentFilterRequest(), new PageableRequest());

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(CommentDto::getContent).containsExactly(content);
        assertThat(filterSearchResult.getContent()).extracting(CommentDto::getId).containsExactly(id);
    }
}
