package com.github.blog.dao.impl;

import com.github.blog.dao.UserDetailDao;
import com.github.blog.model.UserDetail;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDetailDaoImpl extends AbstractJpaDao<UserDetail, Long> implements UserDetailDao {
}