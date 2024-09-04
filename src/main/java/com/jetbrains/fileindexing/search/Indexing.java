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
     * @param watchingFolders the list of directories to be indexed
     */
    void indexAll(List<File> watchingFolders);

    /**
     * Indexes the specified file using the provided search strategy.
     *
     * @param file the file to be indexed
     */
    void putIndex(File file);

    /**
     * Removes the specified file from the index using the provided search strategy.
     *
     * @param file the file to be removed from the index
     */
    void removeIndex(File file);

    /**
     * Searches for files that match the specified term using the provided search strategy.
     *
     * @param term           the term to search for
     * @return a list of files that match the search term
     */
    List<File> search(String term);
}
