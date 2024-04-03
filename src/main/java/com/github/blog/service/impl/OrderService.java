package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderService implements Service<Serializable> {

    private final Dao<Order> orderDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderService(Dao<Order> orderDao, ObjectMapper objectMapper) {
        this.orderDao = orderDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable orderDto) {
        Order order = convertToObject(orderDto);
        enrichOrder(order);
        return orderDao.save(order);
    }

    @Override
    public Serializable readById(int id) {
        Optional<Order> result = orderDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<Order> orders = orderDao.getAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Cannot find any orders");
        }
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable orderDto) {
        Optional<Order> result = orderDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order order = convertToObject(orderDto);
        order.setOrderId(id);
        return orderDao.update(order);
    }

    @Override
    public boolean delete(int id) {
        return orderDao.deleteById(id);
    }

    private Order convertToObject(Serializable orderDto) {
        return objectMapper.convertValue(orderDto, Order.class);
    }

    private Serializable convertToDto(Order order) {
        return objectMapper.convertValue(order, OrderDto.class);
    }

    private void enrichOrder(Order order) {
        order.setOrderedAt(LocalDateTime.now());
    }
}
