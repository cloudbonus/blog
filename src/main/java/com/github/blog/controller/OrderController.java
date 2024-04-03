package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.OrderDto;
import com.github.blog.service.OrderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderController {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    public int create(OrderDto orderDto) {
        return orderService.create(orderDto);
    }

    public String readById(int id) {
        return convertToJson(orderService.readById(id));
    }

    public String readAll() {
        List<OrderDto> orders = orderService.readAll();
        return convertToJsonArray(orders);
    }

    public OrderDto update(int id, OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    public boolean delete(int id) {
        return orderService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(OrderDto orderDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<OrderDto> orders) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders);
    }
}

