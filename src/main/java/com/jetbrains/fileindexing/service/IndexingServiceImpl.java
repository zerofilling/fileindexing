package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.utils.DaemonThreadFactory;
import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class IndexingServiceImpl implements IndexingService {
    private final ExecutorService executorService;

    public IndexingServiceImpl() {
        this.executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    }

    @Override
    public CompletableFuture<Void> indexAll(List<File> watchingFiles, SearchStrategy searchStrategy) {
        return CompletableFuture.runAsync(() -> {
            TextFileFinder.findTextFiles(watchingFiles, file -> putIndex(file, searchStrategy));
        }, executorService);
    }

    @SneakyThrows
    @Override
    public void putIndex(File file, SearchStrategy searchStrategy) {
        searchStrategy.putIndex(file.getAbsolutePath(), FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    }

    @Override
    public void removeIndex(File file, SearchStrategy searchStrategy) {
        searchStrategy.removeIndex(file.getAbsolutePath());
    }
}
