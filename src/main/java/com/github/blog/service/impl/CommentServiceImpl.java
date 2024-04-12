package com.github.blog.service.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.dto.CommentDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.Comment;
import com.github.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final Mapper mapper;


    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        enrichComment(comment);
        return mapper.map(commentDao.create(comment), CommentDto.class);
    }

    @Override
    public CommentDto findById(int id) {
        Optional<Comment> result = commentDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        return mapper.map(result.get(), CommentDto.class);
    }

    @Override
    public List<CommentDto> findAll() {
        List<Comment> comments = commentDao.findAll();
        if (comments.isEmpty()) {
            throw new RuntimeException("Cannot find any comments");
        }
        return comments.stream().map(c -> mapper.map(c, CommentDto.class)).toList();
    }

    @Override
    public CommentDto update(int id, CommentDto commentDto) {
        Optional<Comment> result = commentDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }

        Comment updatedComment = mapper.map(commentDto, Comment.class);
        Comment comment = result.get();

        updatedComment.setId(comment.getId());
        updatedComment.setPublishedAt(comment.getPublishedAt());

        updatedComment = commentDao.update(updatedComment);

        return mapper.map(updatedComment, CommentDto.class);
    }

    @Override
    public int remove(int id) {
        Comment comment = commentDao.remove(id);
        if (comment == null) {
            return -1;
        } else return comment.getId();
    }

    private void enrichComment(Comment comment) {
        comment.setPublishedAt(LocalDateTime.now());
    }
}

