package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.model.Order;
import com.github.blog.repository.OrderDao;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.OrderErrorResult;
import com.github.blog.service.exception.impl.OrderException;
import com.github.blog.service.mapper.OrderMapper;
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
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(OrderRequest request) {
        Order order = orderMapper.toEntity(request);
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
    public OrderDto update(Long id, OrderRequest request) {
        Order order = orderDao
                .findById(id)
                .orElseThrow(() -> new OrderException(OrderErrorResult.ORDER_NOT_FOUND));

        order = orderMapper.partialUpdate(request, order);
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
