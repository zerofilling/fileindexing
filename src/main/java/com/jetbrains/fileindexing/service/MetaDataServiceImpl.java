package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.param.GetMetadataParam;
import com.jetbrains.fileindexing.param.PutMetadataParam;
import com.jetbrains.fileindexing.repository.MetadataRepository;

import java.io.File;

public class MetaDataServiceImpl implements MetaDataService {
    private final MetadataRepository metadataRepository = FactoryContainer.instance().metadataRepository();
    @Override
    public Long getLastUpdateTime(File dataFolder) {
        return metadataRepository.getLongMetaData(new GetMetadataParam("LastUpdateTime", dataFolder));
    }

    @Override
    public void updateLastTime(File dataFolder) {
        metadataRepository.putLongMetaData(new PutMetadataParam("LastUpdateTime", System.currentTimeMillis(), dataFolder));
    }
}
