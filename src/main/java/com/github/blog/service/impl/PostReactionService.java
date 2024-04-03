package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.PostReactionDto;
import com.github.blog.model.PostReaction;
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
public class PostReactionService implements Service<Serializable> {

    private final Dao<PostReaction> postReactionDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostReactionService(Dao<PostReaction> postReactionDao, ObjectMapper objectMapper) {
        this.postReactionDao = postReactionDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable postReactionDto) {
        PostReaction postReaction = convertToObject(postReactionDto);
        return postReactionDao.save(postReaction);
    }

    @Override
    public Serializable readById(int id) {
        Optional<PostReaction> result = postReactionDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<PostReaction> postReactions = postReactionDao.getAll();
        if (postReactions.isEmpty()) {
            throw new RuntimeException("Cannot find any post reactions");
        }
        return postReactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable postReactionDto) {
        Optional<PostReaction> result = postReactionDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("PostReaction not found");
        }

        PostReaction postReaction = convertToObject(postReactionDto);
        postReaction.setPostId(id);
        return postReactionDao.update(postReaction);
    }

    @Override
    public boolean delete(int id) {
        return postReactionDao.deleteById(id);
    }

    private PostReaction convertToObject(Serializable postReactionDto) {
        return objectMapper.convertValue(postReactionDto, PostReaction.class);
    }

    private Serializable convertToDto(PostReaction postReaction) {
        return objectMapper.convertValue(postReaction, PostReactionDto.class);
    }
}
