package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.RoleRepository;
import com.github.blog.repository.UserInfoRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.UserInfoFilter;
import com.github.blog.repository.specification.UserInfoSpecification;
import com.github.blog.service.UserInfoService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.UserInfoMapper;
import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.UserInfoState;
import com.github.blog.service.util.UserAccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;

    private final UserInfoMapper userInfoMapper;

    private final UserAccessHandler userAccessHandler;
    private final StateMachineService<UserInfoState, UserInfoEvent> stateMachineService;

    @Override
    public UserInfoDto create(UserInfoRequest request) {
        log.debug("Creating a new user info with request: {}", request);
        UserInfo userInfo = userInfoMapper.toEntity(request);

        User user = userRepository
                .findById(userAccessHandler.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USERS_NOT_FOUND));

        userInfo.setUser(user);
        userInfo.setId(user.getId());
        userInfo.setState(UserInfoState.RESERVED.name());

        userInfo = userInfoRepository.save(userInfo);
        log.debug("User info created successfully with ID: {}", userInfo.getId());
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto cancel(Long id) {
        log.debug("Cancelling user info with ID: {}", id);
        UserInfo userInfo = userInfoRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        StateMachine<UserInfoState, UserInfoEvent> sm = stateMachineService.acquireStateMachine(userInfo.getId().toString());
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(UserInfoEvent.CANCEL).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        log.debug("User info cancelled successfully with ID: {}", id);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto verify(Long id, Long roleId) {
        log.debug("Verifying user info with ID: {} and role ID: {}", id, roleId);
        UserInfo userInfo = userInfoRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ORDER_NOT_FOUND));

        User user = userRepository
                .findById(userInfo.getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USERS_NOT_FOUND));

        Role role = roleRepository
                .findById(roleId)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        StateMachine<UserInfoState, UserInfoEvent> sm = stateMachineService.acquireStateMachine(userInfo.getId().toString());
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = Objects.requireNonNull(sm.sendEvent(Mono.just(MessageBuilder.withPayload(UserInfoEvent.VERIFY).build())).blockFirst());

        if (smResult.getResultType().equals(StateMachineEventResult.ResultType.DENIED)) {
            throw new CustomException(ExceptionEnum.STATE_TRANSITION_EXCEPTION);
        }

        log.debug("User info verified successfully with ID: {}", id);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoDto findById(Long id) {
        log.debug("Finding user info by ID: {}", id);
        UserInfo userInfo = userInfoRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        log.debug("User info found with ID: {}", id);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserInfoDto> findAll(UserInfoFilterRequest filterRequest, PageableRequest pageableRequest) {
        log.debug("Finding all user infos with filter: {} and pageable: {}", filterRequest, pageableRequest);
        UserInfoFilter filter = userInfoMapper.toEntity(filterRequest);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<UserInfo> spec = UserInfoSpecification.filterBy(filter);

        Page<UserInfo> userInfos = userInfoRepository.findAll(spec, pageable);

        if (userInfos.isEmpty()) {
            throw new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND);
        }

        log.debug("Found {} user infos", userInfos.getTotalElements());
        return userInfoMapper.toDto(userInfos);
    }

    @Override
    public UserInfoDto update(Long id, UserInfoRequest request) {
        log.debug("Updating user info with ID: {} and request: {}", id, request);
        UserInfo userInfo = userInfoRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        userInfo = userInfoMapper.partialUpdate(request, userInfo);
        log.debug("User info updated successfully with ID: {}", id);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto delete(Long id) {
        log.debug("Deleting user info with ID: {}", id);
        UserInfo userInfo = userInfoRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_INFO_NOT_FOUND));

        userInfoRepository.delete(userInfo);
        log.debug("User info deleted successfully with ID: {}", id);
        return userInfoMapper.toDto(userInfo);
    }

    @Scheduled(fixedRate = 150000)
    private void deleteCanceledUserInfo() {
        log.debug("Deleting canceled user infos");
        List<UserInfo> info = userInfoRepository.findByState(UserInfoState.CANCELED.name());
        info.forEach(userInfo -> {
            userInfoRepository.delete(userInfo);
            log.debug("Deleted canceled user info with ID: {}", userInfo.getId());
        });
    }
}
