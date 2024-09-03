package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexService;

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
     * Creates and returns an instance of {@link IndexService} using the specified database file path.
     *
     * @return an instance of {@link IndexService}
     */
    IndexService indexService();

    /**
     * Creates and returns an instance of {@link IndexRepository} using the specified database file path.
     *
     * @return an instance of {@link IndexRepository}
     */
    IndexRepository indexRepository();

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
