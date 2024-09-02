package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexService;
import com.jetbrains.fileindexing.service.MetaDataService;

public interface BeansAbstractFactory {
    Indexing indexing();

    FileSystemListener fileSystemListener();

    MetaDataService metadataService(String dbFilePath);

    IndexService indexService(String dbFilePath);

    MetadataRepository metadataRepository(String dbFilePath);

    IndexRepository indexRepository(String dbFilePath);

    FileTaxonomyService fileTaxonomyService();

    IndexingFacade indexingFacade();

}
