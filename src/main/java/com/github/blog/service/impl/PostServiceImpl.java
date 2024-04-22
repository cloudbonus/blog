package com.github.blog.service.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.dto.PostDto;
import com.github.blog.model.Post;
import com.github.blog.service.PostService;
import com.github.blog.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final PostMapper postMapper;

    @Override
    public PostDto create(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        enrichPost(post);
        return postMapper.toDto(postDao.create(post));
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postDao.findById(id);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> findAll() {
        List<Post> posts = postDao.findAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("Cannot find any posts");
        }
        return posts.stream().map(postMapper::toDto).toList();
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = postDao.findById(id);

        if (post == null) {
            throw new RuntimeException("Post not found");
        }

        Post updatedPost = postMapper.toEntity(postDto);

        updatedPost.setId(post.getId());
        updatedPost.setPublishedAt(post.getPublishedAt());

        updatedPost = postDao.update(updatedPost);

        return postMapper.toDto(updatedPost);
    }

    @Override
    public void delete(Long id) {
        postDao.deleteById(id);
    }

    private void enrichPost(Post post) {
        post.setPublishedAt(OffsetDateTime.now());
    }
}

