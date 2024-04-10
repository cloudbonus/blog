package com.github.blog.dao.impl;

import com.github.blog.dao.UserDetailsDao;
import com.github.blog.model.UserDetails;
import com.github.blog.util.DefaultConnectionHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDetailsDaoImpl implements UserDetailsDao {

    public final DefaultConnectionHolder connectionHolder;

    private UserDetails extractUserDetails(ResultSet rs) throws SQLException {
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

    @Override
    public Optional<UserDetails> findById(Integer id) {
        String findByIdQuery = "SELECT * FROM blogging_platform.user_details WHERE user_id = ?";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(findByIdQuery);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(extractUserDetails(rs));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<UserDetails> findAll() {
        List<UserDetails> userDetails = new ArrayList<>();
        String findAllQuery = "SELECT * FROM blogging_platform.user_details";
        try {
            Connection conn = connectionHolder.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(findAllQuery);
            while (rs.next()) {
                userDetails.add(extractUserDetails(rs));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userDetails;
    }

    @Override
    public UserDetails create(UserDetails userDetails) {
        String createQuery = "INSERT INTO blogging_platform.user_details(user_id, firstname, surname, university_name, major_name, company_name, job_title) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(createQuery);
            ps.setLong(1, userDetails.getId());
            ps.setString(2, userDetails.getFirstname());
            ps.setString(3, userDetails.getSurname());
            ps.setString(4, userDetails.getUniversityName());
            ps.setString(5, userDetails.getMajorName());
            ps.setString(6, userDetails.getCompanyName());
            ps.setString(7, userDetails.getJobTitle());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                userDetails.setId((int) rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userDetails;
    }

    @Override
    public UserDetails update(UserDetails userDetails) {
        String updateQuery = "UPDATE blogging_platform.user_details SET firstname = ?, surname = ?, university_name = ?, major_name = ?, company_name = ?, job_title = ? WHERE user_id = ?";
        try {
            Connection conn = connectionHolder.getConnection();
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, userDetails.getFirstname());
            ps.setString(2, userDetails.getSurname());
            ps.setString(3, userDetails.getUniversityName());
            ps.setString(4, userDetails.getMajorName());
            ps.setString(5, userDetails.getCompanyName());
            ps.setString(6, userDetails.getJobTitle());
            ps.setLong(7, userDetails.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating userDetails failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userDetails;
    }

    @Override
    public UserDetails remove(Integer id) {
        UserDetails userDetailsToRemove = findById(id).orElse(null);
        String removeQuery = "DELETE FROM blogging_platform.user_details WHERE user_id = ?";
        if (userDetailsToRemove != null) {
            try {
                Connection conn = connectionHolder.getConnection();
                PreparedStatement ps = conn.prepareStatement(removeQuery);
                ps.setLong(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Deleting userDetails failed, no rows affected.");
                }
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userDetailsToRemove;
    }
}