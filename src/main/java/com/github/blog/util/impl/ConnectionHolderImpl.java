package com.github.blog.util.impl;

import com.github.blog.util.ConnectionHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@AllArgsConstructor
public class ConnectionHolderImpl implements DisposableBean, ConnectionHolder {

    private static final Queue<Connection> sharedConnections = new ConcurrentLinkedQueue<>();
    private final DataSource dataSource;
    private final Map<String, Connection> openTransactions = new ConcurrentHashMap<>();

    @Override
    public synchronized Connection getConnection() throws SQLException {
        String threadName = Thread.currentThread().getName();
        if (sharedConnections.isEmpty()) {
            Connection connection = dataSource.getConnection();
            sharedConnections.add(connection);
            return connection;
        }

        Connection connection = sharedConnections.poll();
        try {
            if (connection.getAutoCommit()) {
                return connection;
            } else {
                if (openTransactions.containsKey(threadName)) {
                    if (openTransactions.get(threadName).equals(connection)) {
                        return connection;
                    } else {
                        throw new SQLException("Transaction already closed or aborted.");
                    }
                } else {
                    openTransactions.put(threadName, connection);
                    return connection;
                }
            }
        } catch (SQLException e) {
            sharedConnections.add(connection);
            throw e;
        }
    }

    @Override
    public synchronized void closeConnections() throws SQLException {
        for (Connection connection : sharedConnections) {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        sharedConnections.clear();
        openTransactions.clear();
    }

    @Override
    public void destroy() throws SQLException {
        log.info("Closing all connections");
        closeConnections();
    }
}


