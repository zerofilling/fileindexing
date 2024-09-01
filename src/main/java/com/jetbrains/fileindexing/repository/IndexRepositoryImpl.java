package com.jetbrains.fileindexing.repository;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//todo inject with lightweight, dbFilePath in class state, dbFilePath shouldn't be in each method param
// may be this should be prototype, and IndexService should be injected as lightweight
public class IndexRepositoryImpl implements IndexRepository {

    private void initializeDatabase(String dbFilePath) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS `index` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "key TEXT NOT NULL UNIQUE, " +
                "value TEXT NOT NULL)";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @SneakyThrows
    @Override
    public List<String> search(String term, String dbFilePath) {
        initializeDatabase(dbFilePath);
        List<String> results = new ArrayList<>();
        String querySQL = "SELECT key, value FROM `index` WHERE value LIKE ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, "%" + term + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("key"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
//            todo implement error handling
        }
        return results;
    }

    @SneakyThrows
    @Override
    public void putIndex(String key, String value, String dbFilePath) {
        initializeDatabase(dbFilePath);
        String insertSQL = "INSERT OR REPLACE INTO `index` (key, value) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //            todo implement error handling
        }
    }

    @SneakyThrows
    @Override
    public void removeIndex(String key, String dbFilePath) {
        String deleteSQL = "DELETE FROM `index` WHERE key = ?";
        initializeDatabase(dbFilePath);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //            todo implement error handling
        }
    }
}