package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.PostDto;
import com.github.blog.model.Post;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostService implements Service<Serializable> {

    private final Dao<Post> postDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostService(Dao<Post> postDao, ObjectMapper objectMapper) {
        this.postDao = postDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable postDto) {
        Post post = convertToObject(postDto);
        enrichPost(post);
        return postDao.save(post);
    }

    @Override
    public Serializable readById(int id) {
        Optional<Post> result = postDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<Post> posts = postDao.getAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("Cannot find any posts");
        }
        return posts.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable postDto) {
        Optional<Post> result = postDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Post updatedPost = convertToObject(postDto);
        Post post = result.get();

        updatedPost.setPostId(post.getPostId());
        updatedPost.setPublishedAt(post.getPublishedAt());

        return postDao.update(updatedPost);
    }

    @Override
    public boolean delete(int id) {
        return postDao.deleteById(id);
    }

    private Post convertToObject(Serializable postDto) {
        return objectMapper.convertValue(postDto, Post.class);
    }

    private Serializable convertToDto(Post post) {
        return objectMapper.convertValue(post, PostDto.class);
    }

    private void enrichPost(Post post) {
        post.setPublishedAt(LocalDateTime.now());
    }
}

