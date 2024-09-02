package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.*;

public class MetadataRepositoryImpl implements MetadataRepository {

    private final ConnectionFactory connectionFactory;

    @SneakyThrows
    public MetadataRepositoryImpl(String dbFilePath) {
        connectionFactory = ConnectionFactory.getInstance(dbFilePath);
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS `metadata` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "key TEXT NOT NULL UNIQUE, " +
                "longvalue LONG NOT NULL)";
        try (Connection connection = connectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @Override
    public Long getLongMetaData(String key) throws SQLException {
        String querySQL = "SELECT longvalue FROM metadata WHERE key = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("longvalue");
            }
        }
        return 0L;
    }

    @Override
    public void putLongMetaData(String key, Long value) throws SQLException {
        String insertOrUpdateSQL = "INSERT OR REPLACE INTO metadata (key, longvalue) VALUES (?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertOrUpdateSQL)) {
            pstmt.setString(1, key);
            pstmt.setLong(2, value);
            pstmt.executeUpdate();
        }
    }
}
