package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.PostDao;
import com.github.blog.dto.PostDto;
import com.github.blog.model.Post;
import com.github.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostServiceImpl(PostDao postDao, ObjectMapper objectMapper) {
        this.postDao = postDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(PostDto postDto) {
        Post post = convertToObject(postDto);
        enrichPost(post);
        return postDao.save(post);
    }

    @Override
    public PostDto readById(int id) {
        Optional<Post> result = postDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<PostDto> readAll() {
        List<Post> posts = postDao.getAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("Cannot find any posts");
        }
        return posts.stream().map(this::convertToDto).toList();
    }

    @Override
    public PostDto update(int id, PostDto postDto) {
        Optional<Post> result = postDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Post updatedPost = convertToObject(postDto);
        Post post = result.get();

        updatedPost.setId(post.getId());
        updatedPost.setPublishedAt(post.getPublishedAt());

        result = postDao.update(updatedPost);

        if (result.isEmpty()) {
            throw new RuntimeException("Couldn't update post");
        }

        return convertToDto(result.get());
    }

    @Override
    public boolean delete(int id) {
        return postDao.deleteById(id);
    }

    private Post convertToObject(PostDto postDto) {
        return objectMapper.convertValue(postDto, Post.class);
    }

    private PostDto convertToDto(Post post) {
        return objectMapper.convertValue(post, PostDto.class);
    }

    private void enrichPost(Post post) {
        post.setPublishedAt(LocalDateTime.now());
    }
}

