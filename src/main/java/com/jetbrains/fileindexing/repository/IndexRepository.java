package com.jetbrains.fileindexing.repository;

import java.util.List;

/**
 * The {@code IndexRepository} interface defines the contract for storing and retrieving index data
 * in the file indexing service. Implementations of this interface provide methods to search,
 * add, and remove indexed data.
 */
public interface IndexRepository {

    /**
     * Searches for entries in the index that match the given term.
     *
     * @param term the search term
     * @return a list of matching keys
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
