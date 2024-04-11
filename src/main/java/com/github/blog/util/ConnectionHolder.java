package com.github.blog.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Raman Haurylau
 */
public interface ConnectionHolder {
    Connection getConnection() throws SQLException;

    void closeConnections() throws SQLException;
}
