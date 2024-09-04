package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.facade.IndexingFacadeImpl;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.IndexRepositoryImpl;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.IndexingImpl;
import com.jetbrains.fileindexing.service.*;

public class BeansFactoryImpl implements BeansAbstractFactory {

    // Volatile fields for singleton-like instances
    private Indexing indexing;
    private IndexingFacade indexingFacade;
    private FileTaxonomyService fileTaxonomyService;

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
    public IndexService indexService(Lexer lexer) {
        return new IndexServiceImpl(lexer);
    }


    @Override
    public IndexRepository indexRepository(Lexer lexer) {
        return new IndexRepositoryImpl(lexer);
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
