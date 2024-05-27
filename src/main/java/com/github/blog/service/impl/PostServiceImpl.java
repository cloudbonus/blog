package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Order;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.service.PostService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.PostMapper;
import com.github.blog.service.statemachine.state.OrderState;
import com.github.blog.service.util.UserAccessHandler;
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
    private final UserDao userDao;
    private final PostDao postDao;
    private final OrderDao orderDao;

    private final PostMapper postMapper;
    private final PageableMapper pageableMapper;

    private final UserAccessHandler userAccessHandler;

    @Override
    public PostDto create(PostRequest request) {
        Post post = postMapper.toEntity(request);

        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        post.setUser(user);
        post = postDao.create(post);

        if (userAccessHandler.hasRole("ROLE_COMPANY")) {
            Order order = new Order();
            order.setPost(post);
            order.setUser(post.getUser());
            order.setState(OrderState.NEW.name());

            orderDao.create(order);
        }

        return postMapper.toDto(post);
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        return postMapper.toDto(post);
    }

    @Override
    public PageResponse<PostDto> findAll(PostFilterRequest requestFilter, PageableRequest pageableRequest) {
        PostFilter dtoFilter = postMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Post> posts = postDao.findAll(dtoFilter, pageable);

        if (posts.isEmpty()) {
            throw new CustomException(ExceptionEnum.POSTS_NOT_FOUND);
        }

        return postMapper.toDto(posts);
    }

    @Override
    public PostDto update(Long id, PostRequest request) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        post = postMapper.partialUpdate(request, post);

        return postMapper.toDto(post);
    }

    @Override
    public PostDto delete(Long id) {
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        postDao.delete(post);

        return postMapper.toDto(post);
    }
}

