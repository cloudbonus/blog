package com.github.blog.dao.impl.mapper.impl;

import com.github.blog.dao.impl.mapper.RowMapper;
import com.github.blog.model.Role;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleRowMapperImpl implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId((int) rs.getLong("role_id"));
        role.setName(rs.getString("role_name"));
        return role;
    }
}
