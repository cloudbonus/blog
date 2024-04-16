package com.github.blog.dao.impl.mapper.impl;

import com.github.blog.dao.impl.mapper.RowMapper;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Raman Haurylau
 */
@Component
public class UserRowMapperImpl implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId((int) rs.getLong("user_id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setLastLogin(rs.getTimestamp("last_login").toLocalDateTime());

        String rolesString = rs.getString("roles");
        if (rolesString != null) {
            List<Role> roles = Arrays.stream(rolesString.split(","))
                    .map(roleStr -> {
                        String[] parts = roleStr.split(":");
                        Role role = new Role();
                        role.setId(Integer.parseInt(parts[0]));
                        role.setName(parts[1]);
                        return role;
                    })
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }
        return user;
    }
}
