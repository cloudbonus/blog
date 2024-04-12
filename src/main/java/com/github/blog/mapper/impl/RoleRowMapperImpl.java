package com.github.blog.mapper.impl;

import com.github.blog.mapper.RoleRowMapper;
import com.github.blog.model.Role;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleRowMapperImpl implements RoleRowMapper {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId((int) rs.getLong("role_id"));
        role.setName(rs.getString("role_name"));
        return role;
    }
}
