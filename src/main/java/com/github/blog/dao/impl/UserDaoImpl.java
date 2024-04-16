package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.dao.impl.mapper.RowMapper;
import com.github.blog.model.User;
import com.github.blog.model.UserDetails;
import com.github.blog.util.ConnectionHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private final RowMapper<User> mapper;
    private final ConnectionHolder connectionHolder;

    @Override
    public Optional<User> findById(Integer id) {
        String findByIdQuery = """
                        SELECT u.* , STRING_AGG(CONCAT(r.role_id::text, ':', r.role_name), ',') AS roles
                            FROM blogging_platform.user u
                            JOIN blogging_platform.user_role ur ON u.user_id = ur.user_id
                            JOIN blogging_platform.role r ON ur.role_id = r.role_id
                            WHERE u.user_id = ?
                            group by u.user_id;
                """;
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findByIdQuery);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.mapRow(rs, rs.getRow()));
            }
            ps.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String findAllQuery = """
                        SELECT u.* , STRING_AGG(CONCAT(r.role_id::text, ':', r.role_name), ',') AS roles
                            FROM blogging_platform.user u
                            JOIN blogging_platform.user_role ur ON u.user_id = ur.user_id
                            JOIN blogging_platform.role r ON ur.role_id = r.role_id
                            group by u.user_id;;
                """;
        try {
            Connection conn = connectionHolder.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(findAllQuery);
            while (rs.next()) {
                users.add(mapper.mapRow(rs, rs.getRow()));
            }
            st.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }


    @Override
    public List<User> findAllByRole(String roleName) {
        List<User> users = new ArrayList<>();
        String findAllByRoleQuery = """
                    SELECT u.*,  STRING_AGG(CONCAT(r.role_id::text, ':', r.role_name), ',') AS roles
                        FROM blogging_platform.user u
                        JOIN blogging_platform.user_role ur ON u.user_id = ur.user_id
                        JOIN blogging_platform.role r ON ur.role_id = r.role_id
                        WHERE r.role_name = ?
                        group by u.user_id;
            """;
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findAllByRoleQuery);
            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapper.mapRow(rs, rs.getRow()));
            }
            ps.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public Optional<User> findByLoginAndPassword(UserDetails userDetails) {
        String findByLoginAndPasswordQuery = """
                        SELECT u.* , STRING_AGG(CONCAT(r.role_id::text, ':', r.role_name), ',') AS roles
                            FROM blogging_platform.user u
                            JOIN blogging_platform.user_role ur ON u.user_id = ur.user_id
                            JOIN blogging_platform.role r ON ur.role_id = r.role_id
                            WHERE login = ? and password = ?
                            group by u.user_id;
                """;
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findByLoginAndPasswordQuery);
            ps.setString(1, userDetails.getUser().getLogin());
            ps.setString(2, userDetails.getUser().getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.mapRow(rs, rs.getRow()));
            }
            ps.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllByUniversity(String university) {
        List<User> users = new ArrayList<>();
        String findAllByUniversityQuery = """
                        SELECT u.* , STRING_AGG(CONCAT(r.role_id::text, ':', r.role_name), ',') AS roles
                            FROM blogging_platform.user u
                            JOIN blogging_platform.user_role ur ON u.user_id = ur.user_id
                            JOIN blogging_platform.role r ON ur.role_id = r.role_id
                            JOIN blogging_platform.user_details ud ON u.user_id = ud.user_id
                            WHERE ud.university_name = ?
                            group by u.user_id;
                """;
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findAllByUniversityQuery);
            ps.setString(1, university);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapper.mapRow(rs, rs.getRow()));
            }
            ps.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User create(User user) {
        String createQuery = "INSERT INTO blogging_platform.user (login, password, email, created_at, last_login) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(user.getLastLogin()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId((int) rs.getLong(1));
            }
            ps.close();
            rs.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User update(User user) {
        String updateQuery = "UPDATE blogging_platform.user SET login = ?, password = ?, email = ?, created_at = ?, last_login = ? WHERE user_id = ?";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(user.getLastLogin()));
            ps.setLong(6, user.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            ps.close();
            connectionHolder.release();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User remove(Integer id) {
        User userToRemove = findById(id).orElse(null);
        String removeQuery = "DELETE FROM blogging_platform.user WHERE user_id = ?";
        if (userToRemove != null) {
            try {
                Connection conn = connectionHolder.getConnection();
                PreparedStatement ps = conn.prepareStatement(removeQuery);
                ps.setLong(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Deleting user failed, no rows affected.");
                }
                ps.close();
                connectionHolder.release();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userToRemove;
    }
}
