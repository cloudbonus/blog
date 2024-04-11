package com.github.blog.util.impl;

import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
public class ResultSetFactory {
    public static Role createRole(ResultSet rs) throws SQLException {
        return extractRole(rs);
    }

    public static UserDetails createUserDetails(ResultSet rs) throws SQLException {
        return extractUserDetails(rs);
    }

    public static User createUser(ResultSet rs) throws SQLException {
        return extractUser(rs);
    }

    private static Role extractRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId((int) rs.getLong("role_id"));
        role.setName("role_name");
        return role;
    }

    private static UserDetails extractUserDetails(ResultSet rs) throws SQLException {
        UserDetails userDetails = new UserDetails();
        userDetails.setId((int) rs.getLong("user_id"));
        userDetails.setFirstname(rs.getString("firstname"));
        userDetails.setSurname(rs.getString("surname"));
        userDetails.setCompanyName(rs.getString("company_name"));
        userDetails.setJobTitle(rs.getString("job_title"));
        userDetails.setMajorName(rs.getString("major_name"));
        userDetails.setUniversityName(rs.getString("university_name"));
        return userDetails;
    }

    private static User extractUser(ResultSet rs) throws SQLException {
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
