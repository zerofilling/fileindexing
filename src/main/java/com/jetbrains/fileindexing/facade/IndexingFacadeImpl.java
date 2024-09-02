package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.utils.TextFileFinder;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IndexingFacadeImpl implements IndexingFacade {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Indexing indexing = FactoryContainer.beansAbstractFactory().indexing();
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.beansAbstractFactory().fileTaxonomyService();

    @Override
    public CompletableFuture<Void> indexAll(List<File> watchingFolders, SearchStrategy searchStrategy) {
        CompletableFuture<Void> addFoldersFuture = CompletableFuture.runAsync(() ->
                watchingFolders.forEach(fileTaxonomyService::addFolder), executorService);
        CompletableFuture<Void> indexFuture = CompletableFuture.runAsync(() ->
                indexing.indexAll(watchingFolders, searchStrategy), executorService);
        return CompletableFuture.allOf(addFoldersFuture, indexFuture);
    }

    @Override
    public void putIndex(File file, SearchStrategy searchStrategy) {
        if (file.isDirectory()) {
            fileTaxonomyService.addFolder(file);
            fileTaxonomyService.visitFiles(file, it -> indexing.putIndex(it, searchStrategy));
        } else if (TextFileFinder.isTextFile(file)) {
            fileTaxonomyService.addFile(file);
            indexing.putIndex(file, searchStrategy);
        }
    }

    @Override
    public void removeIndex(File file, SearchStrategy searchStrategy) {
        indexing.removeIndex(file, searchStrategy);
        fileTaxonomyService.visitFiles(file, it -> indexing.removeIndex(it, searchStrategy));
        fileTaxonomyService.delete(file);
    }

    @Override
    public List<File> search(String term, SearchStrategy searchStrategy) {
        return indexing.search(term, searchStrategy);
    }
}
