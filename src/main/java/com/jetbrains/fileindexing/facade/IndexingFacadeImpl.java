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
    private final Indexing indexing;
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.beansAbstractFactory().fileTaxonomyService();


    public IndexingFacadeImpl(SearchStrategy searchStrategy) {
        this.indexing = FactoryContainer.beansAbstractFactory().indexing(searchStrategy);
    }

    @Override
    public CompletableFuture<Void> indexAll(List<File> watchingFolders) {
        CompletableFuture<Void> addFoldersFuture = CompletableFuture.runAsync(() ->
                watchingFolders.forEach(fileTaxonomyService::addFolder), executorService);
        CompletableFuture<Void> indexFuture = CompletableFuture.runAsync(() ->
                indexing.indexAll(watchingFolders), executorService);
        return CompletableFuture.allOf(addFoldersFuture, indexFuture);
    }

    @Override
    public void putIndex(File file) {
        if (file.isDirectory()) {
            fileTaxonomyService.addFolder(file);
            fileTaxonomyService.visitFiles(file, indexing::putIndex);
        } else if (TextFileFinder.isTextFile(file)) {
            fileTaxonomyService.addFile(file);
            indexing.putIndex(file);
        }
    }

    @Override
    public void removeIndex(File file) {
        indexing.removeIndex(file);
        fileTaxonomyService.visitFiles(file, indexing::removeIndex);
        fileTaxonomyService.delete(file);
    }

    @Override
    public List<File> search(String term) {
        return indexing.search(term);
    }
}
