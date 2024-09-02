package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Statement;

/**
 * The {@code DbInit} class is responsible for initializing the database schema required for the file indexing service.
 * It creates necessary tables if they do not exist and handles the execution of SQL scripts.
 */
public class DbInit {

    /**
     * Initializes the database with the necessary schema.
     * It creates tables for indexing and metadata if they do not already exist.
     *
     * @param dbFilePath the file path to the SQLite database file
     */
    public static void init(String dbFilePath) {
        executeScripts(dbFilePath,
                "CREATE TABLE IF NOT EXISTS `index` (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "key TEXT NOT NULL UNIQUE, " +
                        "value TEXT NOT NULL)",
                "CREATE TABLE IF NOT EXISTS `metadata` (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "key TEXT NOT NULL UNIQUE, " +
                        "longvalue LONG NOT NULL)");
    }

    /**
     * Executes SQL scripts to initialize the database.
     *
     * @param dbFilePath the file path to the SQLite database file
     * @param scripts    the SQL scripts to be executed
     */
    @SneakyThrows
    private static void executeScripts(String dbFilePath, String... scripts) {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance(dbFilePath);
        for (String script : scripts) {
            try (Connection connection = connectionFactory.getConnection();
                 Statement stmt = connection.createStatement()) {
                stmt.execute(script);
            }
        }
    }
}
