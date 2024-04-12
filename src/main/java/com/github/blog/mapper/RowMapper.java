package com.github.blog.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
