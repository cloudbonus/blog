package com.github.blog.dao.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderDaoImpl implements OrderDao {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public Optional<Order> getById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findAny();
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public int save(Order order) {
        orders.add(order);
        int index = orders.size();
        order.setId(index);
        return index;
    }

    @Override
    public Optional<Order> update(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == order.getId()) {
                orders.set(i, order);
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return orders.removeIf(o -> o.getId() == id);
    }
}
