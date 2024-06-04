package com.github.blog.service;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Order;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.OrderServiceImpl;
import com.github.blog.service.mapper.OrderMapper;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.state.OrderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.service.StateMachineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private PostDao postDao;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PageableMapper pageableMapper;

    @Mock
    private StateMachineService<OrderState, OrderEvent> stateMachineService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest request;
    private OrderDto returnedOrderDto;
    private Order order;

    private final Long id = 1L;
    private final Long userId = 1L;
    private final Long postId = 1L;

    private final Pageable pageable = new Pageable();
    private final PageableResponse pageableResponse = new PageableResponse();

    @BeforeEach
    void setUp() {
        User user = new User();
        Post post = new Post();

        request = new OrderRequest();
        request.setPostId(postId);
        request.setUserId(userId);

        order = new Order();
        order.setId(id);
        order.setPost(post);
        order.setUser(user);

        returnedOrderDto = new OrderDto();
        returnedOrderDto.setId(id);
        returnedOrderDto.setPostId(postId);
        returnedOrderDto.setUserId(userId);
    }

    @Test
    @DisplayName("order service: reserve")
    @SuppressWarnings("unchecked")
    void reserve_reservesOrder_whenDataIsValid() {
        StateMachine<OrderState, OrderEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<OrderState, OrderEvent> smResult = mock(StateMachineEventResult.class);

        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(stateMachineService.acquireStateMachine(order.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.ACCEPTED);
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto reservedOrderDto = orderService.reserve(id);

        assertNotNull(reservedOrderDto);
        assertEquals(id, reservedOrderDto.getId());
        assertEquals(postId, reservedOrderDto.getPostId());
    }

    @Test
    @DisplayName("order service: reserve - state transition denied")
    @SuppressWarnings("unchecked")
    void reserve_throwsException_whenStateTransitionDenied() {
        StateMachine<OrderState, OrderEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<OrderState, OrderEvent> smResult = mock(StateMachineEventResult.class);

        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(stateMachineService.acquireStateMachine(order.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.DENIED);

        CustomException exception = assertThrows(CustomException.class, () -> orderService.reserve(id));

        assertEquals(ExceptionEnum.STATE_TRANSITION_EXCEPTION, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("order service: cancel")
    @SuppressWarnings("unchecked")
    void cancel_cancelsOrder_whenDataIsValid() {
        StateMachine<OrderState, OrderEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<OrderState, OrderEvent> smResult = mock(StateMachineEventResult.class);

        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(stateMachineService.acquireStateMachine(order.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.ACCEPTED);
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto cancelledOrderDto = orderService.cancel(id);

        assertNotNull(cancelledOrderDto);
        assertEquals(id, cancelledOrderDto.getId());
        assertEquals(postId, cancelledOrderDto.getPostId());
    }

    @Test
    @DisplayName("order service: buy")
    @SuppressWarnings("unchecked")
    void buy_completesOrder_whenDataIsValid() {
        StateMachine<OrderState, OrderEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<OrderState, OrderEvent> smResult = mock(StateMachineEventResult.class);

        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(stateMachineService.acquireStateMachine(order.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.ACCEPTED);
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto boughtOrderDto = orderService.buy(id);

        assertNotNull(boughtOrderDto);
        assertEquals(id, boughtOrderDto.getId());
        assertEquals(postId, boughtOrderDto.getPostId());
    }

    @Test
    @DisplayName("order service: find by id")
    void findById_returnsOrderDto_whenIdIsValid() {
        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto foundOrderDto = orderService.findById(id);

        assertNotNull(foundOrderDto);
        assertEquals(id, foundOrderDto.getId());
        assertEquals(postId, foundOrderDto.getPostId());
    }

    @Test
    @DisplayName("order service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(orderDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.findById(id));

        assertEquals(ExceptionEnum.ORDER_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("order service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        OrderFilter filter = new OrderFilter();

        Page<Order> page = new Page<>(Collections.singletonList(order), pageable, 1L);
        PageResponse<OrderDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedOrderDto), pageableResponse, 1L);

        when(orderMapper.toEntity(any(OrderFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(orderDao.findAll(filter, pageable)).thenReturn(page);
        when(orderMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<OrderDto> foundOrders = orderService.findAll(new OrderFilterRequest(), new PageableRequest());

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.getContent().size());
        assertEquals(id, foundOrders.getContent().get(0).getId());
        assertEquals(postId, foundOrders.getContent().get(0).getPostId());
    }

    @Test
    @DisplayName("order service: find all - not found exception")
    void findAll_throwsException_whenNoOrdersFound() {
        OrderFilter filter = new OrderFilter();

        Page<Order> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(orderMapper.toEntity(any(OrderFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(orderDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> orderService.findAll(new OrderFilterRequest(), new PageableRequest()));

        assertEquals(ExceptionEnum.ORDERS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("order service: update")
    void update_returnsUpdatedOrderDto_whenDataIsValid() {
        User user = new User();
        Post post = new Post();

        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(postDao.findById(postId)).thenReturn(Optional.of(post));
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto updatedOrderDto = orderService.update(id, request);

        assertNotNull(updatedOrderDto);
        assertEquals(id, updatedOrderDto.getId());
        assertEquals(postId, updatedOrderDto.getPostId());
    }

    @Test
    @DisplayName("order service: delete")
    void delete_returnsDeletedOrderDto_whenIdIsValid() {
        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(returnedOrderDto);

        OrderDto deletedOrderDto = orderService.delete(id);

        assertNotNull(deletedOrderDto);
        assertEquals(id, deletedOrderDto.getId());
        assertEquals(postId, deletedOrderDto.getPostId());
        verify(orderDao, times(1)).delete(order);
    }
}
