package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.model.CommentReaction;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionService implements Service<Serializable> {

    private final Dao<CommentReaction> commentReactionDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionService(Dao<CommentReaction> commentReactionDao, ObjectMapper objectMapper) {
        this.commentReactionDao = commentReactionDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable commentReactionDto) {
        CommentReaction commentReaction = convertToObject(commentReactionDto);
        return commentReactionDao.save(commentReaction);
    }

    @Override
    public Serializable readById(int id) {
        Optional<CommentReaction> result = commentReactionDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<CommentReaction> commentReactions = commentReactionDao.getAll();
        if (commentReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any comment reactions");
        }
        return commentReactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable commentReactionDto) {
        Optional<CommentReaction> result = commentReactionDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }

        CommentReaction updatedCommentReaction = convertToObject(commentReactionDto);
        CommentReaction commentReaction = result.get();

        updatedCommentReaction.setCommentId(commentReaction.getCommentId());

        return commentReactionDao.update(updatedCommentReaction);
    }

    @Override
    public boolean delete(int id) {
        return commentReactionDao.deleteById(id);
    }

    private CommentReaction convertToObject(Serializable commentReactionDto) {
        return objectMapper.convertValue(commentReactionDto, CommentReaction.class);
    }

    private Serializable convertToDto(CommentReaction commentReaction) {
        return objectMapper.convertValue(commentReaction, CommentReactionDto.class);
    }
}

