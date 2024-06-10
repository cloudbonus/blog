package com.github.blog.config.statemachine;

import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.EnumSet;


/**
 * @author Raman Haurylau
 */
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "orderStateMachineFactory")
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    private final StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachinePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        config.withPersistence().runtimePersister(stateMachinePersister);
        config.withConfiguration().autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderState.NEW)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions.withExternal().source(OrderState.NEW).target(OrderState.RESERVED).event(OrderEvent.RESERVE)
                .and()
                .withExternal().source(OrderState.RESERVED).target(OrderState.CANCELED).event(OrderEvent.CANCEL)
                .and()
                .withExternal().source(OrderState.RESERVED).target(OrderState.COMPLETED).event(OrderEvent.BUY);
    }
}
