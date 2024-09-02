package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//todo inject with lightweight, dbFilePath in class state, dbFilePath shouldn't be in each method param
// may be this should be prototype, and IndexService should be injected as lightweight
public class IndexRepositoryImpl implements IndexRepository {

    private final ConnectionFactory connectionFactory;

    @SneakyThrows
    public IndexRepositoryImpl(String dbFilePath) {
        connectionFactory = ConnectionFactory.getInstance(dbFilePath);
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS `index` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "key TEXT NOT NULL UNIQUE, " +
                "value TEXT NOT NULL)";
        try (Connection connection = connectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @Override
    public List<String> search(String term) throws SQLException {
        List<String> results = new ArrayList<>();
        String querySQL = "SELECT key, value FROM `index` WHERE value LIKE ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, "%" + term + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("key"));
            }
        }
        return results;
    }

    @Override
    public void putIndex(String key, String value) throws SQLException {
        String insertSQL = "INSERT OR REPLACE INTO `index` (key, value) VALUES (?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void removeIndex(String key) throws SQLException {
        String deleteSQL = "DELETE FROM `index` WHERE key = ?";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        }
    }
}