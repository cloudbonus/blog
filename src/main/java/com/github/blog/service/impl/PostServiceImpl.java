package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostDtoFilter;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Post;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.service.PostService;
import com.github.blog.service.exception.PostErrorResult;
import com.github.blog.service.exception.impl.PostException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final PostMapper postMapper;
    private final PageableMapper pageableMapper;

    @Override
    public PostDto create(PostRequest request) {
        Post post = postMapper.toEntity(request);
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
    public Page<PostDto> findAll(PostDtoFilter requestFilter, PageableRequest pageableRequest) {
        PostFilter dtoFilter = postMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<Post> posts = postDao.findAll(dtoFilter, pageable);

        if (posts.isEmpty()) {
            throw new PostException(PostErrorResult.POSTS_NOT_FOUND);
        }

        return posts.map(postMapper::toDto);
    }

    @Override
    public PostDto update(Long id, PostRequest request) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new PostException(PostErrorResult.POST_NOT_FOUND));

        post = postMapper.partialUpdate(request, post);
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

