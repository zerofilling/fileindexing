package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class IndexingFacadeImpl implements IndexingFacade {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Indexing indexing;
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.beansAbstractFactory().fileTaxonomyService();


    public IndexingFacadeImpl(final SearchStrategy searchStrategy) {
        this.indexing = FactoryContainer.beansAbstractFactory().indexing(searchStrategy);
    }

    @Override
    public CompletableFuture<Void> indexAll(final List<File> watchingFolders) {
        final CompletableFuture<Void> addFoldersFuture = CompletableFuture.runAsync(() ->
                watchingFolders.forEach(fileTaxonomyService::addFolder), executorService);
        final CompletableFuture<Void> indexFuture = CompletableFuture.runAsync(() ->
                indexing.indexAll(watchingFolders), executorService);
        return CompletableFuture.allOf(addFoldersFuture, indexFuture);
    }

    @Override
    public void indexFile(final File file) {
        try {
            if (file.isDirectory()) {
                fileTaxonomyService.addFolder(file);
                fileTaxonomyService.visitFiles(file, indexing::indexFile);
            } else if (TextFileFinder.isTextFile(file)) {
                fileTaxonomyService.addFile(file);
                indexing.indexFile(file);
            }
        } catch(SecurityException e) {
            log.error("Access denied. Could not read file or directory: {}", file);
        }
    }

    @Override
    public void removeIndex(final File file) {
        indexing.removeIndex(file);
        fileTaxonomyService.visitFiles(file, indexing::removeIndex);
        fileTaxonomyService.delete(file);
    }

    @Override
    public List<File> search(final String term) {
        return indexing.search(term);
    }
}
