package com.github.blog.repository.impl;

import com.github.blog.repository.UserDetailDao;
import com.github.blog.model.UserDetail;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDetailDaoImpl extends AbstractJpaDao<UserDetail, Long> implements UserDetailDao {
}