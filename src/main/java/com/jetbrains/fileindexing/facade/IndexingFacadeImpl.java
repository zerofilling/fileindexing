package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexingService;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IndexingFacadeImpl implements IndexingFacade {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final IndexingService indexingService = FactoryContainer.instance().indexingService();
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.instance().fileTaxonomyService();

    @Override
    public CompletableFuture<Void> indexAll(List<File> watchingFolders, SearchStrategy searchStrategy) {
        CompletableFuture<Void> addFoldersFuture = CompletableFuture.runAsync(() ->
                watchingFolders.forEach(fileTaxonomyService::addFolder), executorService);
        CompletableFuture<Void> indexFuture = CompletableFuture.runAsync(() ->
                indexingService.indexAll(watchingFolders, searchStrategy), executorService);
        return CompletableFuture.allOf(addFoldersFuture, indexFuture);
    }

    @Override
    public void putIndex(File file, SearchStrategy searchStrategy) {
        if (file.isDirectory()) {
            fileTaxonomyService.addFolder(file);
            fileTaxonomyService.visitFiles(file, it -> indexingService.putIndex(it, searchStrategy));
        } else {
            fileTaxonomyService.addFile(file);
            indexingService.putIndex(file, searchStrategy);
        }
    }

    @Override
    public void removeIndex(File file, SearchStrategy searchStrategy) {
        indexingService.removeIndex(file, searchStrategy);
        fileTaxonomyService.visitFiles(file, it -> indexingService.removeIndex(it, searchStrategy));
        fileTaxonomyService.delete(file);
    }
}
