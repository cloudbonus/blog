package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.OrderDao;
import com.github.blog.dto.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ObjectMapper objectMapper) {
        this.orderDao = orderDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = convertToObject(orderDto);
        enrichOrder(order);
        return convertToDto(orderDao.create(order));
    }

    @Override
    public OrderDto findById(int id) {
        Optional<Order> result = orderDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderDao.findAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Cannot find any orders");
        }
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto update(int id, OrderDto orderDto) {
        Optional<Order> result = orderDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order updatedOrder = convertToObject(orderDto);
        Order order = result.get();

        updatedOrder.setOrderedAt(order.getOrderedAt());
        updatedOrder.setId(order.getId());

        updatedOrder = orderDao.update(updatedOrder);

        return convertToDto(updatedOrder);
    }

    @Override
    public int remove(int id) {
        Order order = orderDao.remove(id);
        if (order == null) {
            return -1;
        } else return order.getId();
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
