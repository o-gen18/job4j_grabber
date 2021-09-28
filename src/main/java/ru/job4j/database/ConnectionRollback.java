package ru.job4j.database;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Used for rolling back all commits in test cases.
 */
public class ConnectionRollback {
    public static Connection create(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        return (Connection) Proxy.newProxyInstance(
                ConnectionRollback.class.getClassLoader(),
                new Class[] {Connection.class},
                (proxy, method, args) -> {
                    Object result = null;
                    if ("close".equals(method.getName())) {
                        connection.rollback();
                        connection.close();
                    } else {
                        result = method.invoke(connection, args);
                    }
                    return result;
                }
        );
    }
}
