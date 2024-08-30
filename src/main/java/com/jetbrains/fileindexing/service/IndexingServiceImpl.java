package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class IndexingServiceImpl implements IndexingService {

    @Override
    public CompletableFuture<Void> indexAll(File watchingFolder, SearchStrategy searchStrategy) {
        return CompletableFuture.supplyAsync(() -> {
            TextFileFinder.findTextFiles(watchingFolder, file -> putIndex(file, searchStrategy));
            return null;
        });
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
