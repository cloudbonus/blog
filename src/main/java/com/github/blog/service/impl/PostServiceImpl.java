package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Log4j2
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

    private static final String ROLE_COMPANY = "ROLE_COMPANY";

    @Override
    public PostDto create(PostRequest request) {
        log.info("Creating a new post with request: {}", request);
        Post post = postMapper.toEntity(request);

        User user = userDao
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userAccessHandler.getUserId());
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        post.setUser(user);
        post = postDao.create(post);

        if (userAccessHandler.hasRole(ROLE_COMPANY)) {
            Order order = new Order();
            order.setPost(post);
            order.setUser(post.getUser());
            order.setState(OrderState.NEW.name());

            orderDao.create(order);
            log.info("Created a new order for post: {}", post.getId());
        }

        log.info("Post created successfully with ID: {}", post.getId());
        return postMapper.toDto(post);
    }

    @Override
    public PostDto findById(Long id) {
        log.info("Finding post by ID: {}", id);
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_NOT_FOUND);
                });

        log.info("Post found with ID: {}", id);
        return postMapper.toDto(post);
    }

    @Override
    public PageResponse<PostDto> findAll(PostFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.info("Finding all posts with filter: {} and pageable: {}", requestFilter, pageableRequest);
        PostFilter filter = postMapper.toEntity(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Post> posts = postDao.findAll(filter, pageable);

        if (posts.isEmpty()) {
            log.error("No posts found with the given filter and pageable");
            throw new CustomException(ExceptionEnum.POSTS_NOT_FOUND);
        }

        log.info("Found {} posts", posts.getTotalNumberOfEntities());
        return postMapper.toDto(posts);
    }

    @Override
    public PostDto update(Long id, PostRequest request) {
        log.info("Updating post with ID: {} and request: {}", id, request);
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_NOT_FOUND);
                });

        post = postMapper.partialUpdate(request, post);
        log.info("Post updated successfully with ID: {}", id);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto delete(Long id) {
        log.info("Deleting post with ID: {}", id);
        Post post = postDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.POST_NOT_FOUND);
                });

        postDao.delete(post);
        log.info("Post deleted successfully with ID: {}", id);
        return postMapper.toDto(post);
    }
}

