package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Order;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.OrderMapper;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final PostDao postDao;

    private final OrderMapper orderMapper;
    private final PageableMapper pageableMapper;

    private final StateMachineService<OrderState, OrderEvent> stateMachineService;

    @Override
    public OrderDto reserve(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.RESERVE).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto cancel(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.CANCEL).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto buy(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.BUY).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        return orderMapper.toDto(order);
    }

    @Override
    public PageResponse<OrderDto> findAll(OrderFilterRequest requestFilter, PageableRequest pageableRequest) {
        OrderFilter dtoFilter = orderMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Order> orders = orderDao.findAll(dtoFilter, pageable);

        if (orders.isEmpty()) {
            throw new CustomException(ExceptionEnum.ORDERS_NOT_FOUND);
        }

        return orderMapper.toDto(orders);
    }

    @Override
    public OrderDto update(Long id, OrderRequest request) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        User user = userDao
                .findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        Post post = postDao
                .findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.POST_NOT_FOUND));

        order.setUser(user);
        order.setPost(post);

        return orderMapper.toDto(order);
    }

    @Scheduled(fixedRate = 150000)
    private void deleteInactiveOrders() {
        List<Order> orders = orderDao.findAllInactiveOrders();
        orders.forEach(orderDao::delete);
    }

    @Override
    public OrderDto findByPostId(Long id) {
        Order order = orderDao
                .findByPostId(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto delete(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        orderDao.delete(order);

        return orderMapper.toDto(order);
    }
}
