package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDetailDto;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.UserDetailDao;
import com.github.blog.service.UserDetailService;
import com.github.blog.service.exception.UserDetailErrorResult;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserDetailException;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.mapper.UserDetailMapper;
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
public class UserDetailsServiceImpl implements UserDetailService {
    private final UserDao userDao;
    private final UserDetailDao userDetailDao;
    private final UserDetailMapper detailMapper;

    @Override
    public UserDetailDto create(UserDetailDto userDetailsDto) {
        UserDetail userDetail = detailMapper.toEntity(userDetailsDto);

        User user = userDao
                .findById(userDetail.getId())
                .orElseThrow(() -> new UserException(UserErrorResult.USERS_NOT_FOUND));

        userDetail.setUser(user);
        userDetail.setId(user.getId());
        userDetail = userDetailDao.create(userDetail);

        return detailMapper.toDto(userDetail);
    }

    @Override
    public UserDetailDto findById(Long id) {
        UserDetail userDetail = userDetailDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));

        return detailMapper.toDto(userDetail);
    }

    @Override
    public List<UserDetailDto> findAll() {
        List<UserDetail> userDetails = userDetailDao.findAll();

        if (userDetails.isEmpty()) {
            throw new UserDetailException(UserDetailErrorResult.USER_DETAILS_NOT_FOUND);
        }

        return userDetails.stream().map(detailMapper::toDto).toList();
    }

    @Override
    public UserDetailDto update(Long id, UserDetailDto userDetailsDto) {
        UserDetail userDetail = userDetailDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));

        userDetail = detailMapper.partialUpdate(userDetailsDto, userDetail);
        userDetail = userDetailDao.update(userDetail);

        return detailMapper.toDto(userDetail);
    }

    @Override
    public UserDetailDto delete(Long id) {
        UserDetail userDetail = userDetailDao
                .findById(id)
                .orElseThrow(() -> new UserDetailException(UserDetailErrorResult.USER_DETAIL_NOT_FOUND));
        userDetailDao.delete(userDetail);
        return detailMapper.toDto(userDetail);
    }
}
