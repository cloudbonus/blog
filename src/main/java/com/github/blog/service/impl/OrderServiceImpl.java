package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Order;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.OrderException;
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
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final PageableMapper pageableMapper;
    private final StateMachineService<OrderState, OrderEvent> stateMachineService;

    @Override
    public OrderDto reserve(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.RESERVE).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new OrderException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto cancel(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.CANCEL).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new OrderException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto buy(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<OrderState, OrderEvent> sm = stateMachineService.acquireStateMachine(order.getId().toString());
        StateMachineEventResult<OrderState, OrderEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvent.BUY).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new OrderException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> findAll(OrderDtoFilter requestFilter, PageableRequest pageableRequest) {
        OrderFilter dtoFilter = orderMapper.toDto(requestFilter);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<Order> orders = orderDao.findAll(dtoFilter, pageable);

        if (orders.isEmpty()) {
            throw new OrderException(ExceptionEnum.ORDERS_NOT_FOUND);
        }

        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderDto update(Long id, OrderRequest request) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        order = orderMapper.partialUpdate(request, order);

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
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto delete(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(ExceptionEnum.ORDER_NOT_FOUND));

        orderDao.delete(order);

        return orderMapper.toDto(order);
    }
}
