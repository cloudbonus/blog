package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.CommentReactionDao;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.model.CommentReaction;
import com.github.blog.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class CommentReactionServiceImpl implements CommentReactionService {

    private final CommentReactionDao commentReactionDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentReactionServiceImpl(CommentReactionDao commentReactionDao, ObjectMapper objectMapper) {
        this.commentReactionDao = commentReactionDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public CommentReactionDto create(CommentReactionDto commentReactionDto) {
        CommentReaction commentReaction = convertToObject(commentReactionDto);
        return convertToDto(commentReactionDao.create(commentReaction));
    }

    @Override
    public CommentReactionDto findById(int id) {
        Optional<CommentReaction> result = commentReactionDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<CommentReactionDto> findAll() {
        List<CommentReaction> commentReactions = commentReactionDao.findAll();
        if (commentReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any comment reactions");
        }
        return commentReactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public CommentReactionDto update(int id, CommentReactionDto commentReactionDto) {
        Optional<CommentReaction> result = commentReactionDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }

        CommentReaction updatedCommentReaction = convertToObject(commentReactionDto);
        CommentReaction commentReaction = result.get();

        updatedCommentReaction.setId(commentReaction.getId());

        updatedCommentReaction = commentReactionDao.update(updatedCommentReaction);

        return convertToDto(updatedCommentReaction);
    }

    @Override
    public int remove(int id) {
        CommentReaction commentReaction = commentReactionDao.remove(id);
        if (commentReaction == null) {
            return -1;
        } else return commentReaction.getId();
    }

    private CommentReaction convertToObject(CommentReactionDto commentReactionDto) {
        return objectMapper.convertValue(commentReactionDto, CommentReaction.class);
    }

    private CommentReactionDto convertToDto(CommentReaction commentReaction) {
        return objectMapper.convertValue(commentReaction, CommentReactionDto.class);
    }
}

