package com.github.blog.repository.impl;

import com.github.blog.model.UserInfo;
import com.github.blog.repository.UserInfoDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserInfoDaoImpl extends AbstractJpaDao<UserInfo, Long> implements UserInfoDao {
}