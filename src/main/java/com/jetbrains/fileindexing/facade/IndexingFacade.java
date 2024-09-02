package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.search.SearchStrategy;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The {@code IndexingFacade} interface provides a high-level abstraction for
 * file indexing operations. It defines methods for indexing files, removing
 * files from the index, and searching indexed files using a specified
 * {@link SearchStrategy}.
 */
public interface IndexingFacade {

    /**
     * Indexes all files in the specified directories using the provided search strategy.
     * This method returns a {@link CompletableFuture} that completes when the indexing process is finished.
     *
     * @param watchingFolders the list of directories to be indexed
     * @param searchStrategy  the strategy to use for indexing and searching files
     * @return a {@link CompletableFuture} that completes when the indexing process is done
     */
    CompletableFuture<Void> indexAll(List<File> watchingFolders, SearchStrategy searchStrategy);

    /**
     * Indexes the specified file using the provided search strategy.
     *
     * @param file            the file to be indexed
     * @param searchStrategy  the strategy to use for indexing the file
     */
    void putIndex(File file, SearchStrategy searchStrategy);

    /**
     * Removes the specified file from the index using the provided search strategy.
     *
     * @param file            the file to be removed from the index
     * @param searchStrategy  the strategy to use for removing the file from the index
     */
    void removeIndex(File file, SearchStrategy searchStrategy);

    /**
     * Searches for files that match the specified term using the provided search strategy.
     *
     * @param term            the term to search for
     * @param searchStrategy  the strategy to use for searching the indexed files
     * @return a list of files that match the search term
     */
    List<File> search(String term, SearchStrategy searchStrategy);
}
