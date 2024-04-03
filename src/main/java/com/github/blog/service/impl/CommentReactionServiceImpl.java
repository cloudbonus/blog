package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.CommentReactionDao;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.model.CommentReaction;
import com.github.blog.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionServiceImpl implements CommentReactionService {

    private final CommentReactionDao commentReactionDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionServiceImpl(CommentReactionDao commentReactionDao, ObjectMapper objectMapper) {
        this.commentReactionDao = commentReactionDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(CommentReactionDto commentReactionDto) {
        CommentReaction commentReaction = convertToObject(commentReactionDto);
        return commentReactionDao.save(commentReaction);
    }

    @Override
    public CommentReactionDto readById(int id) {
        Optional<CommentReaction> result = commentReactionDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<CommentReactionDto> readAll() {
        List<CommentReaction> commentReactions = commentReactionDao.getAll();
        if (commentReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any comment reactions");
        }
        return commentReactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public CommentReactionDto update(int id, CommentReactionDto commentReactionDto) {
        Optional<CommentReaction> result = commentReactionDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }

        CommentReaction updatedCommentReaction = convertToObject(commentReactionDto);
        CommentReaction commentReaction = result.get();

        updatedCommentReaction.setId(commentReaction.getId());

        result = commentReactionDao.update(updatedCommentReaction);

        if (result.isEmpty()) {
            throw new RuntimeException("Couldn't update comment reaction");
        }

        return convertToDto(result.get());
    }

    @Override
    public boolean delete(int id) {
        return commentReactionDao.deleteById(id);
    }

    private CommentReaction convertToObject(CommentReactionDto commentReactionDto) {
        return objectMapper.convertValue(commentReactionDto, CommentReaction.class);
    }

    private CommentReactionDto convertToDto(CommentReaction commentReaction) {
        return objectMapper.convertValue(commentReaction, CommentReactionDto.class);
    }
}

