package com.jetbrains.fileindexing.search;

import java.util.List;

public interface SearchStrategy {

    void putIndex(String key, String value);

    List<String> search(String term);

    void removeIndex(String key);

    long getIndexedTime();

    void putIndexedTime();

    void cleanDb();
}
