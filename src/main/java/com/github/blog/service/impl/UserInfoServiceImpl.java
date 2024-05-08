package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.UserInfoDao;
import com.github.blog.service.UserInfoService;
import com.github.blog.service.exception.UserDetailErrorResult;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserDetailException;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserDao userDao;
    private final UserInfoDao userInfoDao;
    private final UserInfoMapper detailMapper;

    @Override
    public UserInfoDto create(UserInfoDto request) {
        UserInfo userInfo = detailMapper.toEntity(request);

        User user = userDao
                .findById(userInfo.getId())
                .orElseThrow(() -> new UserException(UserErrorResult.USERS_NOT_FOUND));

        userInfo.setUser(user);
        userInfo.setId(user.getId());
        userInfo = userInfoDao.create(userInfo);

        return detailMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto findById(Long id) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));

        return detailMapper.toDto(userInfo);
    }

    @Override
    public List<UserInfoDto> findAll() {
        List<UserInfo> userInfos = userInfoDao.findAll();

        if (userInfos.isEmpty()) {
            throw new UserDetailException(UserDetailErrorResult.USER_DETAILS_NOT_FOUND);
        }

        return userInfos.stream().map(detailMapper::toDto).toList();
    }

    @Override
    public UserInfoDto update(Long id, UserInfoDto request) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));

        userInfo = detailMapper.partialUpdate(request, userInfo);
        userInfo = userInfoDao.update(userInfo);

        return detailMapper.toDto(userInfo);
    }

    @Override
    public UserInfoDto delete(Long id) {
        UserInfo userInfo = userInfoDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));
        userInfoDao.delete(userInfo);
        return detailMapper.toDto(userInfo);
    }
}
