package com.jetbrains.fileindexing.processor;

import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.IndexingService;
import com.jetbrains.fileindexing.service.SearchService;
import com.jetbrains.fileindexing.utils.SearchNotReadyException;
import com.jetbrains.fileindexing.utils.Status;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class FileSearch {
    private final Config config;
    private final IndexingService indexingService = FactoryContainer.instance().indexingService();
    private final FileSystemListener fileSystemListener = FactoryContainer.instance().fileSystemListener();
    private final SearchService searchService = FactoryContainer.instance().searchService();
    private volatile AtomicReference<Status> status = new AtomicReference<>(Status.INDEXING);

    @Builder
    private FileSearch(Config config) {
        assert config != null;
        this.config = config;
        initialize();
    }

    public List<File> search(String term) {
        if (Objects.equals(status, Status.INDEXING)) {
            throw new SearchNotReadyException();
        }
        return searchService.search(term, config.getSearchStrategy());
    }

    private void initialize() {
        assert config != null;
        log.info("Initializing file search...");
        status.set(Status.INDEXING);
        SearchStrategy searchStrategy = config.getSearchStrategy();
        CompletableFuture<Void> feature = indexingService.indexAll(config.getWatchingFolders(), searchStrategy);
        feature.whenComplete((unused, exception) -> status.set(exception == null ? Status.READY : Status.FAILED));
        fileSystemListener.listenFilesChanges(config.getWatchingFolders(),
                file -> indexingService.putIndex(file, searchStrategy),
                file -> indexingService.removeIndex(file, searchStrategy));
    }
}

