package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.search.SearchStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.List;

/**
 * The {@code Config} class encapsulates the configuration for the file indexing service.
 * It holds the list of directories to watch for changes and the search strategy to use
 * when querying the indexed files.
 * <p>
 * This class is immutable and uses the builder pattern to provide a flexible way to create instances.
 * </p>
 */
@Getter
@ToString
public final class Config {

    /**
     * A list of directories to be watched for changes.
     */
    private final List<File> watchingFolders;

    /**
     * The strategy used to search within the indexed files.
     */
    private final SearchStrategy searchStrategy;

    /**
     * Constructs a {@code Config} instance with the specified watching folders and search strategy.
     *
     * @param watchingFolders  the list of directories to watch for changes; must not be null
     * @param searchStrategy   the search strategy to use; must not be null
     * @throws IllegalArgumentException if {@code watchingFolders} or {@code searchStrategy} is null
     */
    @Builder
    private Config(List<File> watchingFolders, SearchStrategy searchStrategy) {
        assert watchingFolders != null;
        assert searchStrategy != null;
        this.watchingFolders = watchingFolders;
        this.searchStrategy = searchStrategy;
    }
}
