package com.github.blog.config.statemachine;

import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.UserInfoState;
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
@EnableStateMachineFactory(name = "config2")
@RequiredArgsConstructor
public class UserInfoStateMachineConfig extends EnumStateMachineConfigurerAdapter<UserInfoState, UserInfoEvent> {
    private final StateMachineRuntimePersister<UserInfoState, UserInfoEvent, String> stateMachinePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<UserInfoState, UserInfoEvent> config) throws Exception {
        config.withPersistence().runtimePersister(stateMachinePersister);
        config.withConfiguration().autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<UserInfoState, UserInfoEvent> states) throws Exception {
        states.withStates()
                .initial(UserInfoState.RESERVED)
                .states(EnumSet.allOf(UserInfoState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<UserInfoState, UserInfoEvent> transitions) throws Exception {
        transitions
                .withExternal().source(UserInfoState.RESERVED).target(UserInfoState.CANCELED).event(UserInfoEvent.CANCEL)
                .and()
                .withExternal().source(UserInfoState.RESERVED).target(UserInfoState.VERIFIED).event(UserInfoEvent.VERIFY);
    }
}
