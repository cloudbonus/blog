package com.github.blog.service.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.dto.common.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.OrderErrorResult;
import com.github.blog.service.exception.impl.OrderException;
import com.github.blog.service.mapper.OrderMapper;
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
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order.setOrderedAt(OffsetDateTime.now());
        return orderMapper.toDto(orderDao.create(order));
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorResult.ORDER_NOT_FOUND));

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderDao.findAll();

        if (orders.isEmpty()) {
            throw new OrderException(OrderErrorResult.ORDERS_NOT_FOUND);
        }

        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto update(Long id, OrderDto orderDto) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorResult.ORDER_NOT_FOUND));

        order = orderMapper.partialUpdate(orderDto, order);
        order = orderDao.update(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto delete(Long id) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorResult.ORDER_NOT_FOUND));

        orderDao.delete(order);
        return orderMapper.toDto(order);
    }
}
