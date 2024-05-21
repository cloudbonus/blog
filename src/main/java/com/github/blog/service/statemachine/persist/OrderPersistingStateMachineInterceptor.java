package com.github.blog.service.statemachine.persist;


import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.persist.AbstractPersistingStateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Component;


/**
 * @author Raman Haurylau
 */
@Component
@RequiredArgsConstructor
public class OrderPersistingStateMachineInterceptor extends AbstractPersistingStateMachineInterceptor<OrderState, OrderEvent, String>
        implements StateMachineRuntimePersister<OrderState, OrderEvent, String> {
    private final OrderStateMachinePersist persist;

    @Override
    public void write(StateMachineContext<OrderState, OrderEvent> context, String contextObj) {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<OrderState, OrderEvent> read(String contextObj) {
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<OrderState, OrderEvent> getInterceptor() {
        return this;
    }

}
