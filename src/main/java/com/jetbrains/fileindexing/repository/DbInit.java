package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Statement;

public class DbInit {

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
