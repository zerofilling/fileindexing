package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndexRepositoryImpl implements IndexRepository {

    private final ConnectionFactory connectionFactory;

    public IndexRepositoryImpl(String dbFilePath) throws SQLException {
        connectionFactory = ConnectionFactory.getInstance(dbFilePath);
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