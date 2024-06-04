package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Order;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;
import com.github.blog.service.statemachine.state.OrderState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-order-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class OrderDaoImplTests {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
        pageable.setOrderBy("ASC");
    }

    @Test
    @Rollback
    @DisplayName("order dao: create")
    void create_returnsOrder_whenDataIsValid() {
        Optional<User> optionalUser = userDao.findById(1L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        Optional<Post> optionalPost = postDao.findById(1L);
        assertThat(optionalPost).isPresent();
        Post post = optionalPost.get();

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setPost(post);
        newOrder.setState(OrderState.NEW.name());
        Order createdOrder = orderDao.create(newOrder);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(createdOrder.getPost().getId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("order dao: find by id")
    void findById_returnsOrder_whenIdIsValid() {
        Optional<Order> foundOrder = orderDao.findById(1L);

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(1L);
    }

    @Test
    @Rollback
    @DisplayName("order dao: update")
    void update_returnsUpdatedOrder_whenDataIsValid() {
        Optional<Order> optionalOrder = orderDao.findById(1L);
        assertThat(optionalOrder).isPresent();
        Order updatedOrder = optionalOrder.get();

        updatedOrder.setState(OrderState.RESERVED.name());

        updatedOrder = orderDao.update(updatedOrder);

        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getId()).isEqualTo(1L);
        assertThat(updatedOrder.getState()).isEqualTo(OrderState.RESERVED.name());
    }

    @Test
    @Rollback
    @DisplayName("order dao: delete")
    void delete_deletesOrder_whenIdIsValid() {
        Optional<Order> optionalOrder = orderDao.findById(1L);
        assertThat(optionalOrder).isPresent();

        Order deletedOrder = optionalOrder.get();

        orderDao.delete(deletedOrder);

        assertThat(orderDao.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("order dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedOrders() {
        Page<Order> ordersPage = orderDao.findAll(new OrderFilter(), pageable);

        assertThat(ordersPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("order dao: find by post id")
    void findByPostId_returnsOrder_whenPostIdIsValid() {
        Optional<Order> foundOrder = orderDao.findByPostId(5L);

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("order dao: find all inactive orders")
    void findAllInactiveOrders_returnsInactiveOrders() {
        List<Order> inactiveOrders = orderDao.findAllInactiveOrders();

        assertThat(inactiveOrders).isNotEmpty();
    }
}
