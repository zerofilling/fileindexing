package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import lombok.SneakyThrows;

import java.io.File;

public class MetaDataServiceImpl implements MetaDataService {
    private final MetadataRepository metadataRepository;

    public MetaDataServiceImpl(String dbFilePath) {
        metadataRepository = FactoryContainer.beansAbstractFactory().metadataRepository(dbFilePath);
    }

    @SneakyThrows //  todo throw custom service exception
    @Override
    public Long getLastUpdateTime(File dataFolder) {
        return metadataRepository.getLongMetaData("LastUpdateTime");
    }

    @Override
    @SneakyThrows //  todo throw custom service exception
    public void updateLastTime(File dataFolder) {
        metadataRepository.putLongMetaData("LastUpdateTime", System.currentTimeMillis());
    }
}
