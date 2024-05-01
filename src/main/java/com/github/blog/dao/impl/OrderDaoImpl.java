package com.github.blog.dao.impl;

import com.github.blog.dao.OrderDao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class OrderDaoImpl extends AbstractJpaDao<Order, Long> implements OrderDao {
}
