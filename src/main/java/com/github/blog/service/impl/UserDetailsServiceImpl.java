package com.github.blog.service.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserDetailDao;
import com.github.blog.dto.UserDetailDto;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import com.github.blog.service.UserDetailService;
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

        User user = userDao.findById(userDetail.getId());
        userDetail.setUser(user);
        userDetail.setId(user.getId());
        userDetail = userDetailDao.create(userDetail);

        return detailMapper.toDto(userDetail);
    }

    @Override
    public UserDetailDto findById(Long id) {
        UserDetail userDetail = userDetailDao.findById(id);
        if (userDetail == null) {
            throw new RuntimeException("User Details not found");
        }
        return detailMapper.toDto(userDetail);
    }

    @Override
    public List<UserDetailDto> findAll() {
        List<UserDetail> userDetails = userDetailDao.findAll();
        if (userDetails.isEmpty()) {
            throw new RuntimeException("Cannot find any user details");
        }
        return userDetails.stream().map(detailMapper::toDto).toList();
    }

    @Override
    public UserDetailDto update(Long id, UserDetailDto userDetailsDto) {
        UserDetail userDetail = userDetailDao.findById(id);

        if (userDetail == null) {
            throw new RuntimeException("User Detail not found");
        }

        UserDetail updatedUserDetails = detailMapper.toEntity(userDetailsDto);

        updatedUserDetails.setId(userDetail.getId());

        updatedUserDetails = userDetailDao.update(updatedUserDetails);

        return detailMapper.toDto(updatedUserDetails);
    }

    @Override
    public void delete(Long id) {
        userDetailDao.deleteById(id);
    }
}
