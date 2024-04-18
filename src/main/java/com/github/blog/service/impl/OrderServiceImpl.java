package com.github.blog.service.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.dto.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.model.mapper.OrderMapper;
import com.github.blog.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        enrichOrder(order);
        return orderMapper.toDto(orderDao.create(order));
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderDao.findAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Cannot find any orders");
        }
        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto update(Long id, OrderDto orderDto) {
        Order order = orderDao.findById(id);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        Order updatedOrder = orderMapper.toEntity(orderDto);

        updatedOrder.setOrderedAt(order.getOrderedAt());
        updatedOrder.setId(order.getId());

        updatedOrder = orderDao.update(updatedOrder);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public void delete(Long id) {
        orderDao.deleteById(id);
    }

    private void enrichOrder(Order order) {
        order.setOrderedAt(OffsetDateTime.now());
    }
}
