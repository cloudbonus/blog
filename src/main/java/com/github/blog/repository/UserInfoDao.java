package com.github.blog.repository;

import com.github.blog.model.UserInfo;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserInfoFilter;

/**
 * @author Raman Haurylau
 */
public interface UserInfoDao extends CrudDao<UserInfo, Long> {
    Page<UserInfo> findAll(UserInfoFilter filter, Pageable pageable);
}
