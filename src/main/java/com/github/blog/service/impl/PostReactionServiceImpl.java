package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.PostReactionDao;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.model.PostReaction;
import com.github.blog.service.PostReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class PostReactionServiceImpl implements PostReactionService {

    private final PostReactionDao postReactionDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostReactionServiceImpl(PostReactionDao postReactionDao, ObjectMapper objectMapper) {
        this.postReactionDao = postReactionDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public PostReactionDto create(PostReactionDto postReactionDto) {
        PostReaction postReaction = convertToObject(postReactionDto);
        return convertToDto(postReactionDao.create(postReaction));
    }

    @Override
    public PostReactionDto findById(int id) {
        Optional<PostReaction> result = postReactionDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<PostReactionDto> findAll() {
        List<PostReaction> postReactions = postReactionDao.findAll();
        if (postReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any post reactions");
        }
        return postReactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public PostReactionDto update(int id, PostReactionDto postReactionDto) {
        Optional<PostReaction> result = postReactionDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }

        PostReaction updatedPostReaction = convertToObject(postReactionDto);
        PostReaction postReaction = result.get();

        updatedPostReaction.setId(postReaction.getId());

        updatedPostReaction = postReactionDao.update(updatedPostReaction);

        return convertToDto(updatedPostReaction);
    }

    @Override
    public int remove(int id) {
        PostReaction postReaction = postReactionDao.remove(id);
        if (postReaction == null) {
            return -1;
        } else return postReaction.getId();
    }

    private PostReaction convertToObject(PostReactionDto postReactionDto) {
        return objectMapper.convertValue(postReactionDto, PostReaction.class);
    }

    private PostReactionDto convertToDto(PostReaction postReaction) {
        return objectMapper.convertValue(postReaction, PostReactionDto.class);
    }
}
