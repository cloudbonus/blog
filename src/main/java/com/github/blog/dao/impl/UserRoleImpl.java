package com.github.blog.dao.impl;

import com.github.blog.dao.UserRoleDao;
import com.github.blog.model.UserRole;
import com.github.blog.util.ConnectionHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
@Repository
@AllArgsConstructor
public class UserRoleImpl implements UserRoleDao {
    public final ConnectionHolder connectionHolder;

    @Override
    public UserRole create(UserRole userRole) {
        String createQuery = "INSERT INTO blogging_platform.user_role (user_id, role_id) VALUES (?, ?)";
        try {
            Connection con = connectionHolder.getConnection();
            PreparedStatement ps = con.prepareStatement(createQuery);
            ps.setLong(1, userRole.getUserId());
            ps.setLong(2, userRole.getRoleId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userRole;
    }
}
