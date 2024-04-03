package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderDao implements Dao<Order> {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public Optional<Order> getById(int id) {
        return orders.stream().filter(o -> o.getOrderId() == id).findAny();
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public int save(Order order) {
        orders.add(order);
        int index = orders.size();
        order.setOrderId(index);
        return index;
    }

    @Override
    public boolean update(Order order) {
        if (orders.stream().map(o -> o.getOrderId() == order.getOrderId()).findAny().orElse(false)) {
            orders.set(order.getOrderId() - 1, order);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return orders.removeIf(o -> o.getOrderId() == id);
    }
}
