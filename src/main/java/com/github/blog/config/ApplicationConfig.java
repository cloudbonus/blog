package com.github.blog.config;

import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.OrderState;
import com.github.blog.service.statemachine.state.UserInfoState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

/**
 * @author Raman Haurylau
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public StateMachineService<OrderState, OrderEvent> orderPersister(
            StateMachineFactory<OrderState, OrderEvent> stateMachineFactory,
            StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachinePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersister);
    }

    @Bean
    public StateMachineService<UserInfoState, UserInfoEvent> userInfoPersister(
            StateMachineFactory<UserInfoState, UserInfoEvent> stateMachineFactory,
            StateMachineRuntimePersister<UserInfoState, UserInfoEvent, String> stateMachinePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersister);
    }
}
