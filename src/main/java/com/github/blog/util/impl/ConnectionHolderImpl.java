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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@AllArgsConstructor
public class ConnectionHolderImpl implements DisposableBean, ConnectionHolder {

    private final DataSource dataSource;
    private static final BlockingQueue<Connection> CONNECTION_POOL = new LinkedBlockingQueue<>();
    private static final Map<String, Connection> USED_CONNECTIONS = new ConcurrentHashMap<>();

    @Override
    public Connection getConnection() throws SQLException {
        String threadName = Thread.currentThread().getName();

        Connection connection;
        if (USED_CONNECTIONS.containsKey(threadName)) {
            connection = USED_CONNECTIONS.get(threadName);
        } else {
            connection = CONNECTION_POOL.poll();
            if (connection == null) {
                connection = dataSource.getConnection();
                CONNECTION_POOL.add(connection);
            }
        }

        if (!connection.getAutoCommit() && connection.isClosed()) {
            throw new SQLException("Transaction already closed");
        }

        USED_CONNECTIONS.putIfAbsent(threadName, connection);
        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) throws SQLException {
        String threadName = Thread.currentThread().getName();

        if (connection.getAutoCommit()) {
            USED_CONNECTIONS.remove(threadName);
            CONNECTION_POOL.add(connection);
        }
    }

    public void shutdown() throws SQLException {
        for (Connection connection : USED_CONNECTIONS.values()) {
            releaseConnection(connection);
        }
        for (Connection c : CONNECTION_POOL) {
            c.close();
        }
        CONNECTION_POOL.clear();
    }

    @Override
    public void destroy() throws SQLException {
        log.info("Closing all connections");
        shutdown();
    }
}

