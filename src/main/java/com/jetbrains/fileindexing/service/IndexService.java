package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.List;

public interface IndexService {
    List<String> search(String term, String dbFilePath);

    void putIndex(String key, String value, String dbFilePath);

    void removeIndex(String key, String dbFilePath);
}
