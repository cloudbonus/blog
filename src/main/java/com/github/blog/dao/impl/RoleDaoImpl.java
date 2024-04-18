package com.github.blog.dao.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.model.Role;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public class RoleDaoImpl extends AbstractJpaDao<Role, Long> implements RoleDao {
}

