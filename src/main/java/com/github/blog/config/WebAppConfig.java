package com.github.blog.config;


import com.github.blog.service.statemachine.event.OrderEvent;
import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.OrderState;
import com.github.blog.service.statemachine.state.UserInfoState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Raman Haurylau
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.github.blog")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class WebAppConfig {
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