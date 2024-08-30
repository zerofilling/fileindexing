package com.jetbrains.fileindexing.factory;

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
}
