package com.github.blog.service;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.UserInfoDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserInfoFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.UserInfoServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.UserInfoMapper;
import com.github.blog.service.statemachine.event.UserInfoEvent;
import com.github.blog.service.statemachine.state.UserInfoState;
import com.github.blog.service.util.UserAccessHandler;
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
public class UserInfoServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private UserInfoDao userInfoDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private UserInfoMapper userInfoMapper;

    @Mock
    private PageableMapper pageableMapper;

    @Mock
    private UserAccessHandler userAccessHandler;

    @Mock
    private StateMachineService<UserInfoState, UserInfoEvent> stateMachineService;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    private UserInfoRequest request;
    private UserInfoDto returnedUserInfoDto;
    private UserInfo userInfo;
    private User user;
    private Role role;

    private final Long id = 1L;
    private final Long userId = 1L;
    private final Long roleId = 1L;

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final UserInfoFilterRequest userInfoFilterRequest = new UserInfoFilterRequest(null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

    @BeforeEach
    void setUp() {
        request = new UserInfoRequest(null, null, null, null, null, null);

        userInfo = new UserInfo();
        userInfo.setId(id);

        user = new User();
        user.setId(userId);

        role = new Role();
        role.setId(roleId);

        returnedUserInfoDto = new UserInfoDto(id, null, null, null, null, null, null, null);
    }

    @Test
    @DisplayName("user info service: create")
    void create_createsUserInfo_whenDataIsValid() {
        when(userAccessHandler.getUserId()).thenReturn(userId);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(userInfoMapper.toEntity(request)).thenReturn(userInfo);
        when(userInfoDao.create(userInfo)).thenReturn(userInfo);
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto createdUserInfoDto = userInfoService.create(request);

        assertNotNull(createdUserInfoDto);
        assertEquals(id, createdUserInfoDto.id());
        verify(userInfoDao, times(1)).create(userInfo);
    }

    @Test
    @DisplayName("user info service: cancel")
    @SuppressWarnings("unchecked")
    void cancel_cancelsUserInfo_whenDataIsValid() {
        StateMachine<UserInfoState, UserInfoEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = mock(StateMachineEventResult.class);

        when(userInfoDao.findById(id)).thenReturn(Optional.of(userInfo));
        when(stateMachineService.acquireStateMachine(userInfo.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.ACCEPTED);
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto canceledUserInfoDto = userInfoService.cancel(id);

        assertNotNull(canceledUserInfoDto);
        assertEquals(id, canceledUserInfoDto.id());
    }

    @Test
    @DisplayName("user info service: verify")
    @SuppressWarnings("unchecked")
    void verify_verifiesUserInfo_whenDataIsValid() {
        StateMachine<UserInfoState, UserInfoEvent> sm = mock(StateMachine.class);
        StateMachineEventResult<UserInfoState, UserInfoEvent> smResult = mock(StateMachineEventResult.class);

        when(userInfoDao.findById(id)).thenReturn(Optional.of(userInfo));
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(roleDao.findById(roleId)).thenReturn(Optional.of(role));
        when(stateMachineService.acquireStateMachine(userInfo.getId().toString())).thenReturn(sm);
        when(sm.sendEvent(any(Mono.class))).thenReturn(Flux.just(smResult));
        when(smResult.getResultType()).thenReturn(StateMachineEventResult.ResultType.ACCEPTED);
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto verifiedUserInfoDto = userInfoService.verify(id, roleId);

        assertNotNull(verifiedUserInfoDto);
        assertEquals(id, verifiedUserInfoDto.id());
    }

    @Test
    @DisplayName("user info service: find by id")
    void findById_returnsUserInfoDto_whenIdIsValid() {
        when(userInfoDao.findById(id)).thenReturn(Optional.of(userInfo));
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto foundUserInfoDto = userInfoService.findById(id);

        assertNotNull(foundUserInfoDto);
        assertEquals(id, foundUserInfoDto.id());
    }

    @Test
    @DisplayName("user info service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(userInfoDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userInfoService.findById(id));

        assertEquals(ExceptionEnum.USER_INFO_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("user info service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        UserInfoFilter filter = new UserInfoFilter();

        Page<UserInfo> page = new Page<>(Collections.singletonList(userInfo), pageable, 1L);
        PageResponse<UserInfoDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedUserInfoDto), pageableResponse, 1L);

        when(userInfoMapper.toEntity(any(UserInfoFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userInfoDao.findAll(filter, pageable)).thenReturn(page);
        when(userInfoMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserInfoDto> foundUserInfos = userInfoService.findAll(userInfoFilterRequest, pageableRequest);

        assertNotNull(foundUserInfos);
        assertEquals(1, foundUserInfos.content().size());
        assertEquals(id, foundUserInfos.content().get(0).id());
    }

    @Test
    @DisplayName("user info service: find all - not found exception")
    void findAll_throwsException_whenNoUserInfosFound() {
        UserInfoFilter filter = new UserInfoFilter();

        Page<UserInfo> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(userInfoMapper.toEntity(any(UserInfoFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userInfoDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> userInfoService.findAll(userInfoFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.USER_INFO_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("user info service: update")
    void update_updatesUserInfo_whenDataIsValid() {
        when(userInfoDao.findById(id)).thenReturn(Optional.of(userInfo));
        when(userInfoMapper.partialUpdate(request, userInfo)).thenReturn(userInfo);
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto updatedUserInfoDto = userInfoService.update(id, request);

        assertNotNull(updatedUserInfoDto);
        assertEquals(id, updatedUserInfoDto.id());
    }

    @Test
    @DisplayName("user info service: delete")
    void delete_deletesUserInfo_whenIdIsValid() {
        when(userInfoDao.findById(id)).thenReturn(Optional.of(userInfo));
        when(userInfoMapper.toDto(userInfo)).thenReturn(returnedUserInfoDto);

        UserInfoDto deletedUserInfoDto = userInfoService.delete(id);

        assertNotNull(deletedUserInfoDto);
        assertEquals(id, deletedUserInfoDto.id());
        verify(userInfoDao, times(1)).delete(userInfo);
    }
}
