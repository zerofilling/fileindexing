package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexService;
import com.jetbrains.fileindexing.service.IndexingStatusService;

/**
 * The {@code BeansAbstractFactory} interface defines a factory for creating instances of various services
 * and components used in the file indexing system. This interface follows the abstract factory pattern
 * to allow for different implementations of these services and components.
 */
public interface BeansAbstractFactory {

    /**
     * Creates and returns an instance of {@link Indexing} using the specified search strategy.
     *
     * @param searchStrategy the strategy to be used for searching in the index
     * @return an instance of {@link Indexing}
     */
    Indexing indexing(SearchStrategy searchStrategy);

    /**
     * Creates and returns an instance of {@link FileSystemListener} for monitoring file system changes.
     *
     * @return an instance of {@link FileSystemListener}
     */
    FileSystemListener fileSystemListener();

    /**
     * Creates and returns an instance of {@link IndexService} using the specified lexer.
     *
     * @param lexer the lexer to be used for tokenizing content during indexing
     * @return an instance of {@link IndexService}
     */
    IndexService indexService(Lexer lexer);

    /**
     * Creates and returns an instance of {@link IndexRepository} using the specified lexer.
     *
     * @param lexer the lexer to be used for tokenizing content during indexing
     * @return an instance of {@link IndexRepository}
     */
    IndexRepository indexRepository(Lexer lexer);

    /**
     * Creates and returns an instance of {@link FileTaxonomyService} for managing file taxonomy data.
     *
     * @return an instance of {@link FileTaxonomyService}
     */
    FileTaxonomyService fileTaxonomyService();

    /**
     * Creates and returns an instance of {@link IndexingFacade} using the specified search strategy.
     *
     * @param searchStrategy the strategy to be used for searching in the index
     * @return an instance of {@link IndexingFacade}
     */
    IndexingFacade indexingFacade(SearchStrategy searchStrategy);

    /**
     * Creates and returns an instance of {@link IndexingStatusService} for monitoring the status of indexing operations.
     *
     * @return an instance of {@link IndexingStatusService} that provides functionality for tracking the progress
     *         and status of ongoing indexing processes.
     */
    IndexingStatusService indexingStatusService();

}
