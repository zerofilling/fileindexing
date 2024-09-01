package com.jetbrains.fileindexing.repository;

import java.util.List;

public interface IndexRepository {
    List<String> search(String term, String dbFilePath);

    void putIndex(String key, String value, String dbFilePath);

    void removeIndex(String key, String dbFilePath);
}
