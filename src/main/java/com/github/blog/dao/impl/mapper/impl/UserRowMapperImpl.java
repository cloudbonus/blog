package com.github.blog.dao.impl.mapper.impl;

import com.github.blog.dao.impl.mapper.RowMapper;
import com.github.blog.model.User;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        return user;
    }
}
