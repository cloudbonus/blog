package com.github.blog.util;

import com.github.blog.annotation.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
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
    //    private final DataSource dataSource;
//    private final ThreadLocal<Connection> connections = new ThreadLocal<>();
//
//    public DefaultConnectionHolder(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public Connection getConnection() throws SQLException {
//        Connection con = connections.get();
//        if (con != null) {
//            if (con.isClosed()) {
//                if (isTransaction()) {
//                    throw new SQLException("Cannot reopen a closed connection within a transaction.");
//                } else {
//                    con = dataSource.getConnection();
//                    connections.set(con);
//                }
//            }
//        } else {
//            con = dataSource.getConnection();
//            connections.set(con);
//        }
//
//        return con;
//    }
//
//    public void closeConnection() throws SQLException {
//        Connection con = connections.get();
//        if (con != null) {
//            con.close();
//            connections.remove();
//        }
//    }
//
//    @Override
//    public void destroy() throws SQLException {
//        log.info("Closing all connection");
//        Connection con = connections.get();
//        while (con != null) {
//            closeConnection();
//            con = connections.get();
//        }
//    }
//
//    private boolean isTransaction() {
//        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        for (StackTraceElement element : stackTraceElements) {
//            try {
//                Class<?> clazz = Class.forName(element.getClassName());
//                for (Method method : clazz.getMethods()) {
//                    if (method.getName().equals(element.getMethodName()) && method.isAnnotationPresent(Transaction.class)) {
//                        log.info("find transactional method: {}", method.getName());
//                        return true;
//                    }
//                }
//            } catch (ClassNotFoundException e) {
//                log.error("Error while checking transaction stack trace");
//            }
//        }
//        return false;
//    }
//
    private final DataSource dataSource;
    private final Map<Thread, List<Connection>> connections = new ConcurrentHashMap<>();


    public synchronized Connection getConnection() throws SQLException {
        Thread currentThread = Thread.currentThread();
        if (connections.containsKey(currentThread)) {
            List<Connection> connectionList = connections.get(currentThread);
            for (Connection connection : connectionList) {
                if (!connection.isClosed() && !connection.getAutoCommit()) {
                    return connection;
                }
            }
            Connection connection = dataSource.getConnection();
            connectionList.add(connection);
            return connection;
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

