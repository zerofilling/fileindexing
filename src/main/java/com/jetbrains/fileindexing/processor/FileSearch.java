package com.jetbrains.fileindexing.processor;

import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.utils.SearchNotReadyException;
import com.jetbrains.fileindexing.utils.Status;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The {@code FileSearch} class is responsible for managing the indexing and searching of files
 * in the specified directories. It initializes the indexing process, listens for file system changes,
 * and provides a search function to query the indexed files.
 */
@Slf4j
public class FileSearch {

    private final Config config;
    private final IndexingFacade indexingFacade;
    private final FileSystemListener fileSystemListener = FactoryContainer.beansAbstractFactory().fileSystemListener();
    private final AtomicReference<Status> status = new AtomicReference<>(Status.INDEXING);

    /**
     * Constructs a {@code FileSearch} instance with the specified configuration.
     *
     * @param config the configuration to be used for indexing and searching files
     */
    @Builder
    private FileSearch(Config config) {
        assert config != null;
        this.config = config;
        indexingFacade = FactoryContainer.beansAbstractFactory().indexingFacade(config.getSearchStrategy());
        initialize();
    }

    /**
     * Searches for files that contain the specified term using the configured search strategy.
     *
     * @param term the term to search for
     * @return a list of files that contain the search term
     * @throws SearchNotReadyException if the search is attempted while indexing is still in progress
     */
    public List<File> search(String term) {
        if (Objects.equals(status.get(), Status.INDEXING)) {
            throw new SearchNotReadyException();
        }
        return indexingFacade.search(term);
    }

    /**
     * Initializes the file search by starting the indexing process and setting up
     * listeners for file system changes. The method is called during object construction.
     */
    private void initialize() {
        assert config != null;
        log.info("Initializing file search...");
        status.set(Status.INDEXING);
        CompletableFuture<Void> feature = indexingFacade.indexAll(config.getWatchingFolders());
        feature.whenComplete((unused, exception) -> {
            if (exception == null) {
                status.set(Status.READY);
            } else {
                status.set(Status.FAILED);
            }
            log.info("Index initialization completed, status: '{}'", status.get());
        });
        fileSystemListener.listenFilesChanges(config.getWatchingFolders(),
                indexingFacade::indexFile,
                indexingFacade::removeIndex);
    }

    public Status getStatus() {
        return status.get();
    }
}
