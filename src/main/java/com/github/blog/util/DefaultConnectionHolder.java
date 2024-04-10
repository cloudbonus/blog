package com.github.blog.util;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@AllArgsConstructor
public class DefaultConnectionHolder implements DisposableBean {
    private static final int MAX_POOL_SIZE = 4;
    private final DataSource dataSource;
    private final Map<Thread, List<Connection>> connections = new ConcurrentHashMap<>();

    public synchronized Connection getConnection() throws SQLException {
        Thread currentThread = Thread.currentThread();
        if (connections.containsKey(currentThread)) {
            List<Connection> connectionList = connections.get(currentThread);
            for (Connection connection : connectionList) {
                if (!connection.isClosed()) {
                    if (!connection.getAutoCommit()) {
                        return connection;
                    } else {
                        connection.close();
                        connection = dataSource.getConnection();
                        return connection;
                    }
                }
            }
            if (connectionList.size() < MAX_POOL_SIZE) {
                Connection connection = dataSource.getConnection();
                connectionList.add(connection);
                return connection;
            } else {
                throw new SQLException("Maximum pool size reached");
            }
        } else {
            Connection connection = dataSource.getConnection();
            List<Connection> connectionList = new ArrayList<>();
            connectionList.add(connection);
            connections.put(currentThread, connectionList);
            return connection;
        }
    }

    public synchronized void closeConnections() throws SQLException {
        Thread currentThread = Thread.currentThread();
        if (connections.containsKey(currentThread)) {
            List<Connection> connectionList = connections.get(currentThread);
            for (Connection connection : connectionList) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
            connections.remove(currentThread);
        }
    }

    @Override
    public void destroy() throws SQLException {
        log.info("Closing all connection");
        for (List<Connection> connectionList : connections.values()) {
            for (Connection connection : connectionList) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }
        connections.clear();
    }
}


