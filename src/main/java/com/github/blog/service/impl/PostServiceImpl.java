package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.OrderRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.entity.Order;
import com.github.blog.repository.entity.Post;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.entity.util.OrderState;
import com.github.blog.repository.filter.PostFilter;
import com.github.blog.repository.specification.PostSpecification;
import com.github.blog.service.PostService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PostMapper;
import com.github.blog.service.util.UserAccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final OrderRepository orderRepository;

    private final PostMapper postMapper;

    private final UserAccessHandler userAccessHandler;

    private static final String ROLE_COMPANY = "ROLE_COMPANY";

    @Override
    public PostDto create(PostRequest request) {
        log.debug("Creating a new post with request: {}", request);
        Post post = postMapper.toEntity(request);

        User user = userRepository
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        post.setUser(user);
        post = postRepository.save(post);

        if (userAccessHandler.hasRole(ROLE_COMPANY)) {
            Order order = new Order();
            order.setPost(post);
            order.setUser(post.getUser());
            order.setState(OrderState.NEW);

            orderRepository.save(order);
            log.debug("Created a new order for post: {}", post.getId());
        }

        log.debug("Post created successfully with ID: {}", post.getId());
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto findById(Long id) {
        log.debug("Finding post by ID: {}", id);
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        log.debug("Post found with ID: {}", id);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostDto> findAll(PostFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all posts with filter: {} and pageable: {}", requestFilter, pageableRequest);
        PostFilter filter = postMapper.toEntity(requestFilter);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<Post> spec = PostSpecification.filterBy(filter);
        Page<Post> posts = postRepository.findAll(spec, pageable);

        if (posts.isEmpty()) {
            throw new CustomException(ExceptionEnum.POSTS_NOT_FOUND);
        }

        log.debug("Found {} posts", posts.getTotalElements());
        return postMapper.toDto(posts);
    }

    @Override
    public PostDto update(Long id, PostRequest request) {
        log.debug("Updating post with ID: {} and request: {}", id, request);
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        post = postMapper.partialUpdate(request, post);
        log.debug("Post updated successfully with ID: {}", id);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto delete(Long id) {
        log.debug("Deleting post with ID: {}", id);
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        postRepository.delete(post);
        log.debug("Post deleted successfully with ID: {}", id);
        return postMapper.toDto(post);
    }

    @Scheduled(fixedRate = 150000)
    protected void deleteInactivePosts() {
        log.debug("Deleting inactive posts");
        postRepository.deletePostsByCanceledOrders();
        orderRepository.deleteCanceledOrders();
        log.debug("Successfully deleted canceled posts");
    }
}

