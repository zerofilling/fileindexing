package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.IndexRepositoryImpl;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.repository.MetadataRepositoryImpl;
import com.jetbrains.fileindexing.service.*;

public class FileSystemAbstractFactoryImpl implements FileSystemAbstractFactory {

    @Override
    public IndexingService indexingService() {
        return new IndexingServiceImpl();
    }

    @Override
    public FileSystemListener fileSystemListener() {
        return new FileSystemListenerImpl();
    }

    @Override
    public SearchService searchService() {
        return new SearchServiceImpl();
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
}
