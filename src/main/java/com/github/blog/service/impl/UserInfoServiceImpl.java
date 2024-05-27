package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.etc.VerificationRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.UserInfoDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserInfoFilter;
import com.github.blog.service.UserInfoService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.UserInfoMapper;
import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.UserInfoState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserDao userDao;
    private final UserInfoDao userInfoDao;
    private final RoleDao roleDao;

    private final UserInfoMapper userInfoMapper;
    private final PageableMapper pageableMapper;

    private final StateMachineService<UserInfoState, UserInfoEvent> stateMachineService;

    @Override
    public UserInfoDto create(UserInfoRequest request) {
        UserInfo userInfo = userInfoMapper.toEntity(request);

        User user = userDao
                .findById(userInfo.getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USERS_NOT_FOUND));

        userInfo.setUser(user);
        userInfo.setId(user.getId());
        userInfo.setState(UserInfoState.RESERVED.name());

        userInfo = userInfoDao.create(userInfo);

        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto cancel(Long id) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<UserInfoState, UserInfoEvent> sm = stateMachineService.acquireStateMachine(userInfo.getId().toString());
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(UserInfoEvent.CANCEL).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto verify(Long id, VerificationRequest request) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        User user = userDao
                .findById(userInfo.getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USERS_NOT_FOUND));

        Role role = roleDao
                .findById(request.getRoleId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        StateMachine<UserInfoState, UserInfoEvent> sm = stateMachineService.acquireStateMachine(userInfo.getId().toString());
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(UserInfoEvent.VERIFY).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto findById(Long id) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public PageResponse<UserInfoDto> findAll(UserInfoFilterRequest filterRequest, PageableRequest pageableRequest) {
        UserInfoFilter dtoFilter = userInfoMapper.toDto(filterRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<UserInfo> userInfos = userInfoDao.findAll(dtoFilter, pageable);

        if (userInfos.isEmpty()) {
            throw new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND);
        }

        return userInfoMapper.toDto(userInfos);
    }

    @Override
    public UserInfoDto update(Long id, UserInfoRequest request) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        userInfo = userInfoMapper.partialUpdate(request, userInfo);

        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto delete(Long id) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        userInfoDao.delete(userInfo);

        return userInfoMapper.toDto(userInfo);
    }
}
