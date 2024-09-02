package com.jetbrains.fileindexing.service;

import java.util.List;

/**
 * The {@code IndexService} interface defines the contract for managing the index of files
 * within the file indexing system. Implementations of this interface provide methods
 * to search the index, add new entries to the index, and remove entries from the index.
 */
public interface IndexService {

    /**
     * Searches for entries in the index that match the given term.
     *
     * @param term the search term
     * @return a list of keys that match the search term
     */
    List<String> search(String term);

    /**
     * Adds a new entry to the index.
     *
     * @param key   the key of the entry
     * @param value the value associated with the key
     */
    void putIndex(String key, String value);

    /**
     * Removes an entry from the index.
     *
     * @param key the key of the entry to be removed
     */
    void removeIndex(String key);
}
