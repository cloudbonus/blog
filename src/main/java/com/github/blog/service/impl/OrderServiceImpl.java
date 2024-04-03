package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.OrderDao;
import com.github.blog.dto.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ObjectMapper objectMapper) {
        this.orderDao = orderDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(OrderDto orderDto) {
        Order order = convertToObject(orderDto);
        enrichOrder(order);
        return orderDao.save(order);
    }

    @Override
    public OrderDto readById(int id) {
        Optional<Order> result = orderDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<OrderDto> readAll() {
        List<Order> orders = orderDao.getAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Cannot find any orders");
        }
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto update(int id, OrderDto orderDto) {
        Optional<Order> result = orderDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order updatedOrder = convertToObject(orderDto);
        Order order = result.get();

        updatedOrder.setOrderedAt(order.getOrderedAt());
        updatedOrder.setId(order.getId());

        result = orderDao.update(updatedOrder);

        if (result.isEmpty()) {
            throw new RuntimeException("Couldn't update order");
        }

        return convertToDto(result.get());
    }

    @Override
    public boolean delete(int id) {
        return orderDao.deleteById(id);
    }

    private Order convertToObject(OrderDto orderDto) {
        return objectMapper.convertValue(orderDto, Order.class);
    }

    private OrderDto convertToDto(Order order) {
        return objectMapper.convertValue(order, OrderDto.class);
    }

    private void enrichOrder(Order order) {
        order.setOrderedAt(LocalDateTime.now());
    }
}
