package com.github.blog.service.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.dto.CommentDto;
import com.github.blog.model.Comment;
import com.github.blog.model.mapper.CommentMapper;
import com.github.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final CommentMapper commentMapper;


    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        enrichComment(comment);
        return commentMapper.toDto(commentDao.create(comment));
    }

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentDao.findById(id);
        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> findAll() {
        List<Comment> comments = commentDao.findAll();
        if (comments.isEmpty()) {
            throw new RuntimeException("Cannot find any comments");
        }
        return comments.stream().map(commentMapper::toDto).toList();
    }

    @Override
    public CommentDto update(Long id, CommentDto commentDto) {
        Comment comment = commentDao.findById(id);

        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }

        Comment updatedComment = commentMapper.toEntity(commentDto);

        updatedComment.setId(comment.getId());
        updatedComment.setPublishedAt(comment.getPublishedAt());

        updatedComment = commentDao.update(updatedComment);

        return commentMapper.toDto(updatedComment);
    }

    @Override
    public void delete(Long id) {
        commentDao.deleteById(id);
    }

    private void enrichComment(Comment comment) {
        comment.setPublishedAt(OffsetDateTime.now());
    }
}

