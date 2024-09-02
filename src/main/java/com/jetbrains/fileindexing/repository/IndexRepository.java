package com.jetbrains.fileindexing.repository;

import java.sql.SQLException;
import java.util.List;

public interface IndexRepository {
    List<String> search(String term) throws SQLException;

    void putIndex(String key, String value) throws SQLException;

    void removeIndex(String key) throws SQLException;
}
