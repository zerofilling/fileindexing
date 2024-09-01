package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IndexingService {
    void indexAll(List<File> watchingFolder, SearchStrategy searchStrategy);

    void putIndex(File file, SearchStrategy searchStrategy);

    void removeIndex(File file, SearchStrategy searchStrategy);
}
