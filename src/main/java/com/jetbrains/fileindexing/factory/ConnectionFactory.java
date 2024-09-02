package com.jetbrains.fileindexing.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionFactory {
    private static final Map<String, ConnectionFactory> instances = new ConcurrentHashMap<>();
    private final String dbFilePath;

    private ConnectionFactory(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }

    public static ConnectionFactory getInstance(String dbFilePath) {
        return instances.computeIfAbsent(dbFilePath, ConnectionFactory::new);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
    }
}
