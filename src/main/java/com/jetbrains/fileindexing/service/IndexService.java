package com.jetbrains.fileindexing.service;

import java.util.List;

public interface IndexService {
    List<String> search(String term);

    void putIndex(String key, String value);

    void removeIndex(String key);
}
