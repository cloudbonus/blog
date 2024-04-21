package com.github.blog.service.impl;

import com.github.blog.dao.PostReactionDao;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.model.PostReaction;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.mapper.PostReactionMapper;
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
public class PostReactionServiceImpl implements PostReactionService {

    private final PostReactionDao postReactionDao;
    private final PostReactionMapper postReactionMapper;

    @Override
    public PostReactionDto create(PostReactionDto postReactionDto) {
        PostReaction postReaction = postReactionMapper.toEntity(postReactionDto);
        return postReactionMapper.toDto(postReactionDao.create(postReaction));
    }

    @Override
    public PostReactionDto findById(Long id) {
        PostReaction postReaction = postReactionDao.findById(id);
        if (postReaction == null) {
            throw new RuntimeException("Post reaction not found");
        }
        return postReactionMapper.toDto(postReaction);
    }

    @Override
    public List<PostReactionDto> findAll() {
        List<PostReaction> postReactions = postReactionDao.findAll();
        if (postReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any post reactions");
        }
        return postReactions.stream().map(postReactionMapper::toDto).toList();
    }

    @Override
    public PostReactionDto update(Long id, PostReactionDto postReactionDto) {
        PostReaction postReaction = postReactionDao.findById(id);

        if (postReaction == null) {
            throw new RuntimeException("Post reaction not found");
        }

        PostReaction updatedPostReaction = postReactionMapper.toEntity(postReactionDto);

        updatedPostReaction.setId(postReaction.getId());
        updatedPostReaction = postReactionDao.update(updatedPostReaction);

        return postReactionMapper.toDto(updatedPostReaction);
    }

    @Override
    public void delete(Long id) {
        postReactionDao.deleteById(id);
    }
}
