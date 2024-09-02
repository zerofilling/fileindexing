package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import com.jetbrains.fileindexing.utils.DatabaseCrashedException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.SQLException;

@Slf4j
public class MetaDataServiceImpl implements MetaDataService {
    private final MetadataRepository metadataRepository;

    public MetaDataServiceImpl(String dbFilePath) {
        metadataRepository = FactoryContainer.beansAbstractFactory().metadataRepository(dbFilePath);
    }

    @Override
    public Long getLastUpdateTime(File dataFolder) {
        try {
            return metadataRepository.getLongMetaData("LastUpdateTime").orElse(0L);
        } catch (SQLException e) {
            log.error("", e);
            throw new DatabaseCrashedException(e);
        }
    }

    @Override
    public void updateLastTime(File dataFolder) {
        try {
            metadataRepository.putLongMetaData("LastUpdateTime", System.currentTimeMillis());
        } catch (SQLException e) {
            log.error("", e);
            throw new DatabaseCrashedException(e);
        }
    }
}
