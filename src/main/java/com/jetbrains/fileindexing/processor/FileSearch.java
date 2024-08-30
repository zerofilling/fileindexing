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
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Getter
public class FileSearch {
    private final Config config;
    private final IndexingService indexingService = FactoryContainer.instance().indexingService();
    private final FileSystemListener fileSystemListener = FactoryContainer.instance().fileSystemListener();
    private final SearchService searchService = FactoryContainer.instance().searchService();
    private Status status = Status.INDEXING;

    @Builder
    private FileSearch(Config config, Status status) {
        this.config = config;
        this.status = status;
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
        status = Status.INDEXING;
        SearchStrategy searchStrategy = config.getSearchStrategy();
        CompletableFuture<Void> feature = indexingService.indexAll(config.getWatchingFolder(), searchStrategy);
        feature.thenAccept(unused -> status = Status.READY);
        fileSystemListener.listenFilesChanges(config.getWatchingFolder(),
                file -> indexingService.putIndex(file, searchStrategy),
                file -> indexingService.removeIndex(file, searchStrategy));
    }
}

