package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class MetadataRepositoryImpl implements MetadataRepository {

    private final ConnectionFactory connectionFactory;

    public MetadataRepositoryImpl(String dbFilePath) throws SQLException {
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
    public Optional<Long> getLongMetaData(String key) throws SQLException {
        String querySQL = "SELECT longvalue FROM metadata WHERE key = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getLong("longvalue"));
            }
        }
        return Optional.empty();
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
