package com.github.blog.service.impl;

import com.github.blog.dao.CommentReactionDao;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.CommentReaction;
import com.github.blog.service.CommentReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class CommentReactionServiceImpl implements CommentReactionService {

    private final CommentReactionDao commentReactionDao;
    private final Mapper mapper;

    @Override
    public CommentReactionDto create(CommentReactionDto commentReactionDto) {
        CommentReaction commentReaction = mapper.map(commentReactionDto, CommentReaction.class);

        return mapper.map(commentReactionDao.create(commentReaction), CommentReactionDto.class);
    }

    @Override
    public CommentReactionDto findById(int id) {
        Optional<CommentReaction> result = commentReactionDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }
        return mapper.map(result.get(), CommentReactionDto.class);
    }

    @Override
    public List<CommentReactionDto> findAll() {
        List<CommentReaction> commentReactions = commentReactionDao.findAll();
        if (commentReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any comment reactions");
        }
        return commentReactions.stream().map(c -> mapper.map(c, CommentReactionDto.class)).toList();
    }

    @Override
    public CommentReactionDto update(int id, CommentReactionDto commentReactionDto) {
        Optional<CommentReaction> result = commentReactionDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("CommentReaction not found");
        }

        CommentReaction updatedCommentReaction = mapper.map(commentReactionDto, CommentReaction.class);
        CommentReaction commentReaction = result.get();

        updatedCommentReaction.setId(commentReaction.getId());

        updatedCommentReaction = commentReactionDao.update(updatedCommentReaction);

        return mapper.map(updatedCommentReaction, CommentReactionDto.class);
    }

    @Override
    public int remove(int id) {
        CommentReaction commentReaction = commentReactionDao.remove(id);
        if (commentReaction == null) {
            return -1;
        } else return commentReaction.getId();
    }
}

