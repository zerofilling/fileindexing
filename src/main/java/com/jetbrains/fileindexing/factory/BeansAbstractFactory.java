package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexService;
import com.jetbrains.fileindexing.service.MetaDataService;

/**
 * The {@code BeansAbstractFactory} interface defines a factory for creating instances of various services
 * and components used in the file indexing system. This interface follows the abstract factory pattern
 * to allow for different implementations of these services and components.
 */
public interface BeansAbstractFactory {

    /**
     * Creates and returns an instance of {@link Indexing}.
     *
     * @return an instance of {@link Indexing}
     */
    Indexing indexing();

    /**
     * Creates and returns an instance of {@link FileSystemListener}.
     *
     * @return an instance of {@link FileSystemListener}
     */
    FileSystemListener fileSystemListener();

    /**
     * Creates and returns an instance of {@link MetaDataService} using the specified database file path.
     *
     * @param dbFilePath the path to the database file
     * @return an instance of {@link MetaDataService}
     */
    MetaDataService metadataService(String dbFilePath);

    /**
     * Creates and returns an instance of {@link IndexService} using the specified database file path.
     *
     * @param dbFilePath the path to the database file
     * @return an instance of {@link IndexService}
     */
    IndexService indexService(String dbFilePath);

    /**
     * Creates and returns an instance of {@link MetadataRepository} using the specified database file path.
     *
     * @param dbFilePath the path to the database file
     * @return an instance of {@link MetadataRepository}
     */
    MetadataRepository metadataRepository(String dbFilePath);

    /**
     * Creates and returns an instance of {@link IndexRepository} using the specified database file path.
     *
     * @param dbFilePath the path to the database file
     * @return an instance of {@link IndexRepository}
     */
    IndexRepository indexRepository(String dbFilePath);

    /**
     * Creates and returns an instance of {@link FileTaxonomyService}.
     *
     * @return an instance of {@link FileTaxonomyService}
     */
    FileTaxonomyService fileTaxonomyService();

    /**
     * Creates and returns an instance of {@link IndexingFacade}.
     *
     * @return an instance of {@link IndexingFacade}
     */
    IndexingFacade indexingFacade();

}
