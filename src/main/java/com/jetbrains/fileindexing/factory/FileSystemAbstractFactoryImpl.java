package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.facade.IndexingFacadeImpl;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.IndexRepositoryImpl;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.repository.MetadataRepositoryImpl;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.IndexingImpl;
import com.jetbrains.fileindexing.service.*;

public class FileSystemAbstractFactoryImpl implements FileSystemAbstractFactory {

    @Override
    public Indexing indexing() {
        return new IndexingImpl();
    }

    @Override
    public FileSystemListener fileSystemListener() {
        return new FileSystemListenerImpl();
    }

    @Override
    public IndexRepository indexRepository() {
        return new IndexRepositoryImpl();
    }

    @Override
    public MetaDataService metadataService() {
        return new MetaDataServiceImpl();
    }

    @Override
    public MetadataRepository metadataRepository() {
        return new MetadataRepositoryImpl();
    }

    @Override
    public FileTaxonomyService fileTaxonomyService() {
        return new FileTaxonomyServiceInMemory();
    }

    @Override
    public IndexingFacade indexingFacade() {
        return new IndexingFacadeImpl();
    }

    @Override
    public IndexService indexService() {
        return new IndexServiceImpl();
    }
}
