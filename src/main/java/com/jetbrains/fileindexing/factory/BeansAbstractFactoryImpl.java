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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeansAbstractFactoryImpl implements BeansAbstractFactory {

    // Volatile fields for singleton-like instances
    private volatile Indexing indexing;
    private volatile FileSystemListener fileSystemListener;
    private volatile IndexingFacade indexingFacade;
    private volatile FileTaxonomyService fileTaxonomyService;

    // Maps for caching instances based on dbFilePath
    private final Map<String, MetaDataService> metaDataServiceMap = new ConcurrentHashMap<>();
    private final Map<String, IndexService> indexServiceMap = new ConcurrentHashMap<>();
    private final Map<String, MetadataRepository> metadataRepositoryMap = new ConcurrentHashMap<>();
    private final Map<String, IndexRepository> indexRepositoryMap = new ConcurrentHashMap<>();

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
        if (fileSystemListener == null) {
            synchronized (this) {
                if (fileSystemListener == null) {
                    fileSystemListener = new FileSystemListenerImpl();
                }
            }
        }
        return fileSystemListener;
    }

    @Override
    public MetaDataService metadataService(String dbFilePath) {
        return metaDataServiceMap.computeIfAbsent(dbFilePath, this::createMetaDataService);
    }

    @Override
    public IndexService indexService(String dbFilePath) {
        return indexServiceMap.computeIfAbsent(dbFilePath, this::createIndexService);
    }

    @Override
    public MetadataRepository metadataRepository(String dbFilePath) {
        return metadataRepositoryMap.computeIfAbsent(dbFilePath, this::createMetadataRepository);
    }

    @Override
    public IndexRepository indexRepository(String dbFilePath) {
        return indexRepositoryMap.computeIfAbsent(dbFilePath, this::createIndexRepository);
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

    private MetaDataService createMetaDataService(String dbFilePath) {
        return new MetaDataServiceImpl(dbFilePath);
    }

    private IndexService createIndexService(String dbFilePath) {
        return new IndexServiceImpl(dbFilePath);
    }

    private MetadataRepository createMetadataRepository(String dbFilePath) {
        return new MetadataRepositoryImpl(dbFilePath);
    }

    private IndexRepository createIndexRepository(String dbFilePath) {
        return new IndexRepositoryImpl(dbFilePath);
    }
}
