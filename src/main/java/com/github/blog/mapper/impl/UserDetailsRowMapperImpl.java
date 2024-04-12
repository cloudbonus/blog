package com.github.blog.mapper.impl;

import com.github.blog.mapper.UserDetailsRowMapper;
import com.github.blog.model.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDetailsRowMapperImpl implements UserDetailsRowMapper {
    @Override
    public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
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
}