package com.github.blog.util.impl;

import com.github.blog.config.DataSourceProperties;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@AllArgsConstructor
public class ConnectionHolderImpl implements DisposableBean, ConnectionHolder {

    private final DataSource dataSource;
    private final DataSourceProperties dataSourceProperties;

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final BlockingQueue<Connection> CONNECTION_POOL = new LinkedBlockingQueue<>();
    private static final Map<String, Connection> USED_CONNECTIONS = new ConcurrentHashMap<>();

    @Override
    public synchronized Connection getConnection() throws SQLException {
        String threadName = Thread.currentThread().getName();
        Connection conn;

        if (USED_CONNECTIONS.containsKey(threadName)) {
            conn = USED_CONNECTIONS.get(threadName);
            if (!conn.getAutoCommit() && !conn.isClosed()) {
                return conn;
            }
            if (conn.isClosed()) {
                throw new SQLException("Transaction already closed");
            }
        }

        while (counter.get() == dataSourceProperties.getMaxPoolSize()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SQLException(e);
            }
        }

        conn = CONNECTION_POOL.poll();
        if (conn == null) {
            conn = dataSource.getConnection();
            CONNECTION_POOL.add(conn);
        }

        counter.getAndIncrement();
        USED_CONNECTIONS.putIfAbsent(threadName, conn);

        return conn;
    }


    @Override
    public synchronized void release() {
        String threadName = Thread.currentThread().getName();
        try {
            if (USED_CONNECTIONS.containsKey(threadName) && !USED_CONNECTIONS.get(threadName).getAutoCommit()) {
                Connection conn = USED_CONNECTIONS.get(threadName);
                CONNECTION_POOL.add(conn);
                USED_CONNECTIONS.remove(threadName);
                counter.decrementAndGet();
                notify();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() throws SQLException {
        log.info("Closing all connections");

        for (Connection conn : CONNECTION_POOL) {
            conn.close();
        }
        for (Connection conn : USED_CONNECTIONS.values()) {
            conn.close();
        }
    }
}

