package com.github.blog.dao.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dao.impl.mapper.RowMapper;
import com.github.blog.model.Role;
import com.github.blog.util.ConnectionHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
@AllArgsConstructor
public class RoleDaoImpl implements RoleDao {
    private final RowMapper<Role> mapper;
    private final ConnectionHolder connectionHolder;

    @Override
    public Optional<Role> findById(Integer id) {
        String findByIdQuery = "SELECT * FROM blogging_platform.role WHERE role_id = ?";
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
            connectionHolder.releaseConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName(String name) {
        String findByNameQuery = "SELECT * FROM blogging_platform.role WHERE role_name = ?";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findByNameQuery);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.mapRow(rs, rs.getRow()));
            }
            ps.close();
            rs.close();
            connectionHolder.releaseConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String findAllQuery = "SELECT * FROM blogging_platform.role";
        try {
            Connection conn = connectionHolder.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(findAllQuery);
            while (rs.next()) {
                roles.add(mapper.mapRow(rs, rs.getRow()));
            }
            st.close();
            rs.close();
            connectionHolder.releaseConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    @Override
    public Role create(Role role) {
        String createQuery = "INSERT INTO blogging_platform.role (role_name) VALUES (?)";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, role.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                role.setId((int) rs.getLong(1));
            }
            ps.close();
            rs.close();
            connectionHolder.releaseConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    @Override
    public Role update(Role role) {
        String updateQuery = "UPDATE blogging_platform.role SET role_name = ? WHERE role_id = ?";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, role.getName());
            ps.setLong(2, role.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating role failed, no rows affected.");
            }
            ps.close();
            connectionHolder.releaseConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    @Override
    public Role remove(Integer id) {
        Role roleToRemove = findById(id).orElse(null);
        String removeQuery = "DELETE FROM blogging_platform.role WHERE role_id = ?";
        if (roleToRemove != null) {
            try {
                Connection conn = connectionHolder.getConnection();
                PreparedStatement ps = conn.prepareStatement(removeQuery);
                ps.setLong(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Deleting role failed, no rows affected.");
                }
                ps.close();
                connectionHolder.releaseConnection(conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return roleToRemove;
    }
}

