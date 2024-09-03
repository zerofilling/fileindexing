package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.facade.IndexingFacadeImpl;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.IndexRepositoryImpl;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.IndexingImpl;
import com.jetbrains.fileindexing.service.*;

public class BeansAbstractFactoryImpl implements BeansAbstractFactory {

    // Volatile fields for singleton-like instances
    private volatile Indexing indexing;
    private volatile IndexingFacade indexingFacade;
    private volatile FileTaxonomyService fileTaxonomyService;

    @Override
    public Indexing indexing() {
        if (indexing == null) {
            synchronized (this) {
                if (indexing == null) {
                    indexing = new IndexingImpl();
                }
            }
        }
        return indexing;
    }

    @Override
    public FileSystemListener fileSystemListener() {
        return new FileSystemListenerImpl();
    }

    @Override
    public IndexService indexService() {
        return new IndexServiceImpl();
    }


    @Override
    public IndexRepository indexRepository() {
        return new IndexRepositoryImpl();
    }

    @Override
    public FileTaxonomyService fileTaxonomyService() {
        if (fileTaxonomyService == null) {
            synchronized (this) {
                if (fileTaxonomyService == null) {
                    fileTaxonomyService = new FileTaxonomyServiceInMemory();
                }
            }
        }
        return fileTaxonomyService;
    }

    @Override
    public IndexingFacade indexingFacade() {
        if (indexingFacade == null) {
            synchronized (this) {
                if (indexingFacade == null) {
                    indexingFacade = new IndexingFacadeImpl();
                }
            }
        }
        return indexingFacade;
    }
}
