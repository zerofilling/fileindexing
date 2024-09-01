package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.search.Indexing;
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileTaxonomyService;
import com.jetbrains.fileindexing.service.IndexService;
import com.jetbrains.fileindexing.service.MetaDataService;

public interface FileSystemAbstractFactory {
    Indexing indexing();

    FileSystemListener fileSystemListener();

    IndexRepository indexRepository();

    MetaDataService metadataService();

    MetadataRepository metadataRepository();

    FileTaxonomyService fileTaxonomyService();

    IndexingFacade indexingFacade();

    IndexService indexService();

}
