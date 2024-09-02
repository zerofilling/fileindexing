package com.jetbrains.fileindexing.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code ConnectionFactory} class provides a mechanism for creating and managing
 * database connections. It follows the singleton pattern with a separate instance
 * for each unique database file path. This ensures that each database file has its
 * own dedicated connection factory.
 */
public final class ConnectionFactory {

    private static final Map<String, ConnectionFactory> instances = new ConcurrentHashMap<>();
    private final String dbFilePath;

    /**
     * Private constructor to initialize the {@code ConnectionFactory} with the specified database file path.
     *
     * @param dbFilePath the path to the database file
     */
    private ConnectionFactory(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }

    /**
     * Returns the singleton instance of {@code ConnectionFactory} for the specified database file path.
     * If an instance does not already exist for the given path, a new one is created.
     *
     * @param dbFilePath the path to the database file
     * @return the singleton instance of {@code ConnectionFactory} for the specified path
     */
    public static ConnectionFactory getInstance(String dbFilePath) {
        return instances.computeIfAbsent(dbFilePath, ConnectionFactory::new);
    }

    /**
     * Creates and returns a new {@link Connection} to the database specified by the file path.
     *
     * @return a new {@link Connection} to the database
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
    }
}
