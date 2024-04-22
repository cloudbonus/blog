package com.github.blog.dao.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public class OrderDaoImpl extends AbstractJpaDao<Order, Long> implements OrderDao {
}
