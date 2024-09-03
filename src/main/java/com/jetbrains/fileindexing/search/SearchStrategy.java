package com.jetbrains.fileindexing.search;

import java.util.List;

/**
 * The {@code SearchStrategy} interface defines the contract for implementing various strategies
 * for indexing and searching within the file indexing system. Implementations of this interface
 * provide methods for indexing, searching, and managing the state of the indexed data.
 */
public interface SearchStrategy {

    /**
     * Adds a new entry to the index.
     *
     * @param key   the key of the entry
     * @param value the value associated with the key
     */
    void putIndex(String key, String value);

    /**
     * Searches for entries in the index that match the given term.
     *
     * @param term the search term
     * @return a list of matching keys
     */
    List<String> search(String term);

    /**
     * Removes an entry from the index.
     *
     * @param key the key of the entry to be removed
     */
    void removeIndex(String key);
}
