package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.CommentDao;
import com.github.blog.dto.CommentDto;
import com.github.blog.model.Comment;
import com.github.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao, ObjectMapper objectMapper) {
        this.commentDao = commentDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = convertToObject(commentDto);
        enrichComment(comment);
        return convertToDto(commentDao.create(comment));
    }

    @Override
    public CommentDto findById(int id) {
        Optional<Comment> result = commentDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<CommentDto> findAll() {
        List<Comment> comments = commentDao.findAll();
        if (comments.isEmpty()) {
            throw new RuntimeException("Cannot find any comments");
        }
        return comments.stream().map(this::convertToDto).toList();
    }

    @Override
    public CommentDto update(int id, CommentDto commentDto) {
        Optional<Comment> result = commentDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }

        Comment updatedComment = convertToObject(commentDto);
        Comment comment = result.get();

        updatedComment.setId(comment.getId());
        updatedComment.setPublishedAt(comment.getPublishedAt());

        updatedComment = commentDao.update(updatedComment);

        return convertToDto(updatedComment);
    }

    @Override
    public int remove(int id) {
        Comment comment = commentDao.remove(id);
        if (comment == null) {
            return -1;
        } else return comment.getId();
    }

    private Comment convertToObject(CommentDto commentDto) {
        return objectMapper.convertValue(commentDto, Comment.class);
    }

    private CommentDto convertToDto(Comment comment) {
        return objectMapper.convertValue(comment, CommentDto.class);
    }

    private void enrichComment(Comment comment) {
        comment.setPublishedAt(LocalDateTime.now());
    }
}

