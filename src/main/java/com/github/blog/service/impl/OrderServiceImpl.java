package com.github.blog.service.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.dto.OrderDto;
import com.github.blog.model.Order;
import com.github.blog.service.OrderService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final DefaultMapper mapper;

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = mapper.map(orderDto, Order.class);
        enrichOrder(order);
        return mapper.map(orderDao.create(order), OrderDto.class);
    }

    @Override
    public OrderDto findById(int id) {
        Optional<Order> result = orderDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        return mapper.map(result.get(), OrderDto.class);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderDao.findAll();
        if (orders.isEmpty()) {
            throw new RuntimeException("Cannot find any orders");
        }
        return orders.stream().map(o -> mapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public OrderDto update(int id, OrderDto orderDto) {
        Optional<Order> result = orderDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order updatedOrder = mapper.map(orderDto, Order.class);
        Order order = result.get();

        updatedOrder.setOrderedAt(order.getOrderedAt());
        updatedOrder.setId(order.getId());

        updatedOrder = orderDao.update(updatedOrder);

        return mapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public int remove(int id) {
        Order order = orderDao.remove(id);
        if (order == null) {
            return -1;
        } else return order.getId();
    }

    private void enrichOrder(Order order) {
        order.setOrderedAt(LocalDateTime.now());
    }
}
