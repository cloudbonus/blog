package com.github.blog.dao.impl;

import com.github.blog.dao.UserDetailDao;
import com.github.blog.model.UserDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserDetailDaoImpl extends AbstractJpaDao<UserDetail, Long> implements UserDetailDao {
}