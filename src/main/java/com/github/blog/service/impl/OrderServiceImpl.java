package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PaymentCancelRequest;
import com.github.blog.controller.dto.request.PaymentDto;
import com.github.blog.controller.dto.request.PaymentProcessRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.OrderRepository;
import com.github.blog.repository.PostRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.entity.Order;
import com.github.blog.repository.entity.Post;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.entity.UserInfo;
import com.github.blog.repository.filter.OrderFilter;
import com.github.blog.repository.specification.OrderSpecification;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.OrderMapper;
import com.github.blog.service.util.PaymentRequestProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PostRepository postRepository;
    private final PaymentRequestProducer paymentRequestProducer;
    private final OrderMapper orderMapper;

    @Override
    public PaymentCancelRequest cancel(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        return paymentRequestProducer.sendMessage(new PaymentCancelRequest(order.getId()));
    }

    @Override
    @SneakyThrows
    public PaymentProcessRequest process(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        UserInfo userInfo = order.getUser().getUserInfo();

        String fullName = String.format("%s %s", userInfo.getFirstname(), userInfo.getSurname());

        return paymentRequestProducer.sendMessage(new PaymentProcessRequest(order.getId(), fullName));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        log.debug("Finding order by ID: {}", id);
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        log.debug("Order found with ID: {}", id);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderDto> findAll(OrderFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all orders with filter: {} and pageable: {}", requestFilter, pageableRequest);
        OrderFilter filter = orderMapper.toEntity(requestFilter);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<Order> spec = OrderSpecification.filterBy(filter);
        Page<Order> orders = orderRepository.findAll(spec, pageable);

        if (orders.isEmpty()) {
            throw new CustomException(ExceptionEnum.ORDERS_NOT_FOUND);
        }

        log.debug("Found {} orders", orders.getTotalElements());
        return orderMapper.toDto(orders);
    }

    @Override
    public OrderDto update(Long id, OrderRequest request) {
        log.debug("Updating order with ID: {} and request: {}", id, request);
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        User user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        Post post = postRepository
                .findById(request.postId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        order.setUser(user);
        order.setPost(post);

        log.debug("Order updated successfully with ID: {}", id);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findByPostId(Long id) {
        log.debug("Finding order by post ID: {}", id);
        Order order = orderRepository
                .findByPostId(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        log.debug("Order found with ID: {}", id);
        return orderMapper.toDto(order);
    }

    @Override
    public void updateState(PaymentDto paymentDto) {
        Order order = orderRepository
                .findById(paymentDto.paymentId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        order.setState(paymentDto.state());
    }

    @Override
    public OrderDto delete(Long id) {
        log.debug("Deleting order with ID: {}", id);
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        orderRepository.delete(order);
        log.debug("Order deleted successfully with ID: {}", id);

        return orderMapper.toDto(order);
    }
}