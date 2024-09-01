package com.jetbrains.fileindexing.facade;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexingService;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IndexingFacadeImpl implements IndexingFacade {

    private final IndexingService indexingService = FactoryContainer.instance().indexingService();
    private final FileTaxonomyService fileTaxonomyService = FactoryContainer.instance().fileTaxonomyService();

    @Override
    public CompletableFuture<Void> indexAll(List<File> watchingFolders, SearchStrategy searchStrategy) {
        watchingFolders.forEach(fileTaxonomyService::addFolder); // todo join 2 threads
        return indexingService.indexAll(watchingFolders, searchStrategy);
    }

    @Override
    public void putIndex(File file, SearchStrategy searchStrategy) {
        if (file.isDirectory()) {
            fileTaxonomyService.addFolder(file);
            fileTaxonomyService.visitFiles(file, it -> indexingService.putIndex(it, searchStrategy)); // todo put index if need
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
