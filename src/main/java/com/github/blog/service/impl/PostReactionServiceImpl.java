package com.github.blog.service.impl;

import com.github.blog.dao.PostReactionDao;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.PostReaction;
import com.github.blog.service.PostReactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {

    private final PostReactionDao postReactionDao;
    private final Mapper mapper;

    @Override
    public PostReactionDto create(PostReactionDto postReactionDto) {
        PostReaction postReaction = mapper.map(postReactionDto, PostReaction.class);
        return mapper.map(postReactionDao.create(postReaction), PostReactionDto.class);
    }

    @Override
    public PostReactionDto findById(int id) {
        Optional<PostReaction> result = postReactionDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }
        return mapper.map(result.get(), PostReactionDto.class);
    }

    @Override
    public List<PostReactionDto> findAll() {
        List<PostReaction> postReactions = postReactionDao.findAll();
        if (postReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any post reactions");
        }
        return postReactions.stream().map(p -> mapper.map(p, PostReactionDto.class)).toList();
    }

    @Override
    public PostReactionDto update(int id, PostReactionDto postReactionDto) {
        Optional<PostReaction> result = postReactionDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }

        PostReaction updatedPostReaction = mapper.map(postReactionDto, PostReaction.class);
        PostReaction postReaction = result.get();

        updatedPostReaction.setId(postReaction.getId());

        updatedPostReaction = postReactionDao.update(updatedPostReaction);

        return mapper.map(updatedPostReaction, PostReactionDto.class);
    }

    @Override
    public int remove(int id) {
        PostReaction postReaction = postReactionDao.remove(id);
        if (postReaction == null) {
            return -1;
        } else return postReaction.getId();
    }
}
