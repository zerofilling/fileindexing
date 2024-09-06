package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexingStatusService;
import com.jetbrains.fileindexing.utils.SearchNotReadyException;
import com.jetbrains.fileindexing.utils.Status;
import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class IndexingFacadeImpl implements IndexingFacade {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Indexing indexing;
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.beansAbstractFactory().fileTaxonomyService();
    private final IndexingStatusService indexingStatusService = FactoryContainer.beansAbstractFactory().indexingStatusService();

    public IndexingFacadeImpl(final SearchStrategy searchStrategy) {
        this.indexing = FactoryContainer.beansAbstractFactory().indexing(searchStrategy);
    }

    @Override
    public void indexAll(final List<File> watchingFolders) {
        Future<?> addFoldersFuture = executorService.submit(() ->
                watchingFolders.forEach(fileTaxonomyService::addFolder));
        Future<?> indexFuture = executorService.submit(() ->
                indexing.indexAll(watchingFolders));

        try {
            addFoldersFuture.get();
            indexFuture.get();
            indexingStatusService.success();
            log.info("Index initialization completed, status: '{}'", indexingStatusService.getStatus());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Index initialization error", e);
            indexingStatusService.fail();
        }
    }

    @Override
    public void indexFile(final File file) {
        indexingStatusService.start();
        try {
            if (file.isDirectory()) {
                fileTaxonomyService.addFolder(file);
                fileTaxonomyService.visitFiles(file, indexing::indexFile);
            } else if (TextFileFinder.isTextFile(file)) {
                fileTaxonomyService.addFile(file);
                indexing.indexFile(file);
            }
        } catch (SecurityException e) {
            log.error("Access denied. Could not read file or directory: {}", file);
        }
        indexingStatusService.success();
    }

    @Override
    public void removeIndex(final File file) {
        indexingStatusService.start();
        try {
            indexing.removeIndex(file);
            fileTaxonomyService.visitFiles(file, indexing::removeIndex);
            fileTaxonomyService.delete(file);
        } catch (Exception e) {
            log.error("", e);
        }
        indexingStatusService.success();
    }

    @Override
    public List<File> search(final String term) throws SearchNotReadyException {
        if (indexingStatusService.statusIs(Status.INDEXING)) {
            throw new SearchNotReadyException();
        }
        return indexing.search(term);
    }
}
