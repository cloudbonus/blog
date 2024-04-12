package com.github.blog.service.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.dto.PostDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.Post;
import com.github.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final Mapper mapper;

    @Override
    public PostDto create(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
        enrichPost(post);
        return mapper.map(postDao.create(post), PostDto.class);
    }

    @Override
    public PostDto findById(int id) {
        Optional<Post> result = postDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        return mapper.map(result.get(), PostDto.class);
    }

    @Override
    public List<PostDto> findAll() {
        List<Post> posts = postDao.findAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("Cannot find any posts");
        }
        return posts.stream().map(p -> mapper.map(p, PostDto.class)).toList();
    }

    @Override
    public PostDto update(int id, PostDto postDto) {
        Optional<Post> result = postDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Post updatedPost = mapper.map(postDto, Post.class);
        Post post = result.get();

        updatedPost.setId(post.getId());
        updatedPost.setPublishedAt(post.getPublishedAt());

        updatedPost = postDao.update(updatedPost);

        return mapper.map(updatedPost, PostDto.class);
    }

    @Override
    public int remove(int id) {
        Post post = postDao.remove(id);
        if (post == null) {
            return -1;
        } else return post.getId();
    }

    private void enrichPost(Post post) {
        post.setPublishedAt(LocalDateTime.now());
    }
}

