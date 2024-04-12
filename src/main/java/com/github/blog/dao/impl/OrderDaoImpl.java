package com.github.blog.dao.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class OrderDaoImpl implements OrderDao {
    private static final List<Order> ORDERS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Order> findById(Integer id) {
        return ORDERS.stream().filter(o -> o.getId() == id).findAny();
    }

    @Override
    public List<Order> findAll() {
        return ORDERS;
    }

    @Override
    public Order create(Order order) {
        ORDERS.add(order);
        int index = ORDERS.size();
        order.setId(index);
        return order;
    }

    @Override
    public Order update(Order order) {
        for (int i = 0; i < ORDERS.size(); i++) {
            if (ORDERS.get(i).getId() == order.getId()) {
                ORDERS.set(i, order);
                return order;
            }
        }
        return null;
    }

    @Override
    public Order remove(Integer id) {
        Order orderToRemove = null;

        for (Order o : ORDERS) {
            if (o.getId() == id) {
                orderToRemove = o;
                break;
            }
        }

        if (orderToRemove != null) {
            ORDERS.remove(orderToRemove);
        }

        return orderToRemove;
    }
}
