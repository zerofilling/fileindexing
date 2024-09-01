package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.facade.IndexingFacade;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.service.*;

public interface FileSystemAbstractFactory {
    IndexingService indexingService();
    FileSystemListener fileSystemListener();
    SearchService searchService();
    IndexRepository indexRepository();
    MetaDataService metadataService();
    MetadataRepository metadataRepository();
    FileTaxonomyService fileTaxonomyService();
    IndexingFacade indexingFacade();

}
