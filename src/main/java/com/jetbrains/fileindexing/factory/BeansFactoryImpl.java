package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.facade.IndexingFacadeImpl;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.IndexRepositoryImpl;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.IndexingImpl;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.*;

public class BeansFactoryImpl implements BeansAbstractFactory {

    // fields for singleton-like instances
    private FileTaxonomyService fileTaxonomyService;

    @Override
    public Indexing indexing(final SearchStrategy searchStrategy) {
        return new IndexingImpl(searchStrategy);
    }

    @Override
    public FileSystemListener fileSystemListener() {
        return new FileSystemListenerImpl();
    }

    @Override
    public IndexService indexService(final Lexer lexer) {
        return new IndexServiceImpl(lexer);
    }


    @Override
    public IndexRepository indexRepository(final Lexer lexer) {
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
    public IndexingFacade indexingFacade(final SearchStrategy searchStrategy) {
        return new IndexingFacadeImpl(searchStrategy);
    }
}
