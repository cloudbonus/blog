package com.github.blog.service.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.dto.common.PostDto;
import com.github.blog.dto.filter.PostFilter;
import com.github.blog.dto.request.PostDtoFilter;
import com.github.blog.model.Post;
import com.github.blog.service.PostService;
import com.github.blog.service.exception.PostErrorResult;
import com.github.blog.service.exception.impl.PostException;
import com.github.blog.service.mapper.PostMapper;
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
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final PostMapper postMapper;

    @Override
    public PostDto create(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        return postMapper.toDto(postDao.create(post));
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new PostException(PostErrorResult.POST_NOT_FOUND));

        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> findAll(PostDtoFilter requestFilter) {
        PostFilter dtoFilter = postMapper.toDto(requestFilter);

        List<Post> posts = postDao.findAll(dtoFilter);

        if (posts.isEmpty()) {
            throw new PostException(PostErrorResult.POSTS_NOT_FOUND);
        }

        return posts.stream().map(postMapper::toDto).toList();
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new PostException(PostErrorResult.POST_NOT_FOUND));

        post = postMapper.partialUpdate(postDto, post);
        post = postDao.update(post);

        return postMapper.toDto(post);
    }

    @Override
    public PostDto delete(Long id) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new PostException(PostErrorResult.POST_NOT_FOUND));
        postDao.delete(post);
        return postMapper.toDto(post);
    }
}

