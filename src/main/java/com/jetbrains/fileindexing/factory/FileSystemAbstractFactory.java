package com.jetbrains.fileindexing.factory;

import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.IndexingService;
import com.jetbrains.fileindexing.service.SearchService;

public interface FileSystemAbstractFactory {
    IndexingService indexingService();

    FileSystemListener fileSystemListener();

    SearchService searchService();
}
