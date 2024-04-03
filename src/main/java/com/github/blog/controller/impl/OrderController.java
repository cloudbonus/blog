package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.OrderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderController implements Controller<Serializable> {
    private final Service<Serializable> orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable orderDto) {
        return orderService.create(orderDto);
    }

    public String readById(int id) {
        return convertToJson(orderService.readById(id));
    }

    public String readAll() {
        List<Serializable> orders = orderService.readAll();
        return convertToJsonArray(orders);
    }

    public boolean update(int id, Serializable orderDto) {
        return orderService.update(id, orderDto);
    }

    public boolean delete(int id) {
        return orderService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable orderDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> orders) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders);
    }
}

