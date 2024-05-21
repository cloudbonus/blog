package com.github.blog.service.statemachine.persist;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.blog.model.Order;
import com.github.blog.repository.OrderDao;
import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class OrderStateMachinePersist implements StateMachinePersist<OrderState, OrderEvent, String> {
    private final OrderDao dao;

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer<>());
        kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
        kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
        return kryo;
    });

    @Override
    public void write(StateMachineContext<OrderState, OrderEvent> context, String contextObj) {
        Order order = dao.findById(Long.valueOf(contextObj)).orElse(null);

        if (order == null) {
            log.error("Order not found, unable to update status.");
            return;
        }

        order.setState(context.getState().name());
        order.setStateContext(serialize(context));
        dao.update(order);
    }

    @Override
    public StateMachineContext<OrderState, OrderEvent> read(String contextObj) {
        return dao.findById(Long.valueOf(contextObj)).map(order -> deserialize(order.getStateContext())).orElse(null);
    }

    private String serialize(StateMachineContext<OrderState, OrderEvent> context) {
        return Optional.ofNullable(context).map(ctx -> {
            Kryo kryo = kryoThreadLocal.get();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Output output = new Output(outputStream);
            kryo.writeObject(output, context);
            byte[] bytes = output.toBytes();
            output.flush();
            output.close();
            return Base64.getEncoder().encodeToString(bytes);
        }).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private StateMachineContext<OrderState, OrderEvent> deserialize(String data) {
        return Optional.ofNullable(data).map(str -> {
            Kryo kryo = kryoThreadLocal.get();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            Input input = new Input(inputStream);
            return kryo.readObject(input, StateMachineContext.class);
        }).orElse(null);
    }
}
