package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.CommentDto;
import com.github.blog.model.Comment;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentService implements Service<Serializable> {

    private final Dao<Comment> commentDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentService(Dao<Comment> commentDao, ObjectMapper objectMapper) {
        this.commentDao = commentDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable commentDto) {
        Comment comment = convertToObject(commentDto);
        enrichComment(comment);
        return commentDao.save(comment);
    }

    @Override
    public Serializable readById(int id) {
        Optional<Comment> result = commentDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<Comment> comments = commentDao.getAll();
        if (comments.isEmpty()) {
            throw new RuntimeException("Cannot find any comments");
        }
        return comments.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable commentDto) {
        Optional<Comment> result = commentDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }

        Comment comment = convertToObject(commentDto);
        comment.setCommentId(id);
        return commentDao.update(comment);
    }

    @Override
    public boolean delete(int id) {
        return commentDao.deleteById(id);
    }

    private Comment convertToObject(Serializable commentDto) {
        return objectMapper.convertValue(commentDto, Comment.class);
    }

    private Serializable convertToDto(Comment comment) {
        return objectMapper.convertValue(comment, CommentDto.class);
    }

    private void enrichComment(Comment comment) {
        comment.setPublishedAt(LocalDateTime.now());
    }
}

