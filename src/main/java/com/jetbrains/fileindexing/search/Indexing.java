package com.jetbrains.fileindexing.search;

import java.io.File;
import java.util.List;

/**
 * The {@code Indexing} interface defines the contract for indexing and searching files
 * within the file indexing system. Implementations of this interface provide methods
 * to index files, remove files from the index, and search for files based on a search term.
 */
public interface Indexing {

    /**
     * Indexes all files in the specified directories using the provided search strategy.
     *
     * @param watchingFolder the list of directories to be indexed
     * @param searchStrategy the strategy to use for indexing and searching files
     */
    void indexAll(List<File> watchingFolder, SearchStrategy searchStrategy);

    /**
     * Indexes the specified file using the provided search strategy.
     *
     * @param file the file to be indexed
     * @param searchStrategy the strategy to use for indexing the file
     */
    void putIndex(File file, SearchStrategy searchStrategy);

    /**
     * Removes the specified file from the index using the provided search strategy.
     *
     * @param file the file to be removed from the index
     * @param searchStrategy the strategy to use for removing the file from the index
     */
    void removeIndex(File file, SearchStrategy searchStrategy);

    /**
     * Searches for files that match the specified term using the provided search strategy.
     *
     * @param term the term to search for
     * @param searchStrategy the strategy to use for searching the indexed files
     * @return a list of files that match the search term
     */
    List<File> search(String term, SearchStrategy searchStrategy);
}
