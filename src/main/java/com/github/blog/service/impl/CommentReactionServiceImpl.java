package com.github.blog.service.impl;

import com.github.blog.dao.CommentReactionDao;
import com.github.blog.dto.CommentReactionDto;
import com.github.blog.model.CommentReaction;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.mapper.CommentReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionServiceImpl implements CommentReactionService {

    private final CommentReactionDao commentReactionDao;
    private final CommentReactionMapper commentReactionMapper;

    @Override
    public CommentReactionDto create(CommentReactionDto commentReactionDto) {
        CommentReaction commentReaction = commentReactionMapper.toEntity(commentReactionDto);
        return commentReactionMapper.toDto(commentReactionDao.create(commentReaction));
    }

    @Override
    public CommentReactionDto findById(Long id) {
        CommentReaction commentReaction = commentReactionDao.findById(id);
        if (commentReaction == null) {
            throw new RuntimeException("Comment reaction not found");
        }
        return commentReactionMapper.toDto(commentReaction);
    }

    @Override
    public List<CommentReactionDto> findAll() {
        List<CommentReaction> commentReactions = commentReactionDao.findAll();
        if (commentReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any comment reactions");
        }
        return commentReactions.stream().map(commentReactionMapper::toDto).toList();
    }

    @Override
    public CommentReactionDto update(Long id, CommentReactionDto commentReactionDto) {
        CommentReaction commentReaction = commentReactionDao.findById(id);

        if (commentReaction == null) {
            throw new RuntimeException("Comment reaction not found");
        }

        CommentReaction updatedCommentReaction = commentReactionMapper.toEntity(commentReactionDto);

        updatedCommentReaction.setId(commentReaction.getId());
        updatedCommentReaction = commentReactionDao.update(updatedCommentReaction);

        return commentReactionMapper.toDto(updatedCommentReaction);
    }

    @Override
    public void delete(Long id) {
        commentReactionDao.deleteById(id);
    }
}

