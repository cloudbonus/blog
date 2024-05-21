package com.github.blog.service.statemachine.persist;

import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.UserInfoState;
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
public class UserInfoPersistingStateMachineInterceptor extends AbstractPersistingStateMachineInterceptor<UserInfoState, UserInfoEvent, String>
        implements StateMachineRuntimePersister<UserInfoState, UserInfoEvent, String> {
    private final UserInfoStateMachinePersist persist;

    @Override
    public void write(StateMachineContext<UserInfoState, UserInfoEvent> context, String contextObj) {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<UserInfoState, UserInfoEvent> read(String contextObj) {
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<UserInfoState, UserInfoEvent> getInterceptor() {
        return this;
    }
}
