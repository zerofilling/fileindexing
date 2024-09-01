package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.search.SearchStrategy;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IndexingFacade {
    CompletableFuture<Void> indexAll(List<File> watchingFolders, SearchStrategy searchStrategy);

    void putIndex(File file, SearchStrategy searchStrategy);

    void removeIndex(File file, SearchStrategy searchStrategy);

    List<File> search(String term, SearchStrategy searchStrategy);
}
