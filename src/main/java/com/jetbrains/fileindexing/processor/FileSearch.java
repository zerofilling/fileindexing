package com.jetbrains.fileindexing.processor;

import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.search.SearchStrategy;
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

@Slf4j
public class FileSearch {
    private final Config config;
    private final IndexingFacade indexingFacade = FactoryContainer.beansAbstractFactory().indexingFacade();
    private final FileSystemListener fileSystemListener = FactoryContainer.beansAbstractFactory().fileSystemListener();
    private final AtomicReference<Status> status = new AtomicReference<>(Status.INDEXING);

    @Builder
    private FileSearch(Config config) {
        assert config != null;
        this.config = config;
        initialize();
    }

    public List<File> search(String term) {
        if (Objects.equals(status.get(), Status.INDEXING)) {
            throw new SearchNotReadyException();
        }
        return indexingFacade.search(term, config.getSearchStrategy());
    }

    private void initialize() {
        assert config != null;
        log.info("Initializing file search...");
        status.set(Status.INDEXING);
        SearchStrategy searchStrategy = config.getSearchStrategy();
        CompletableFuture<Void> feature = indexingFacade.indexAll(config.getWatchingFolders(), searchStrategy);
        feature.whenComplete((unused, exception) -> {
            status.set(exception == null ? Status.READY : Status.FAILED);
            log.info("Index initialization completed, status: '{}'", status.get());
        });
        fileSystemListener.listenFilesChanges(config.getWatchingFolders(),
                file -> indexingFacade.putIndex(file, searchStrategy),
                file -> indexingFacade.removeIndex(file, searchStrategy));
        System.out.println("");
    }
}

