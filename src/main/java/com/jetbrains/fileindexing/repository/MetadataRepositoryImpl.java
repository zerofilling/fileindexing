package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.param.GetMetadataParam;
import com.jetbrains.fileindexing.param.PutMetadataParam;
import com.jetbrains.fileindexing.utils.PropertyFileUtils;

import java.io.File;

public class MetadataRepositoryImpl implements MetadataRepository {
    @Override
    public Long getLongMetaData(GetMetadataParam param) {
        return Long.parseLong(PropertyFileUtils.get(param.key(), "0", getMetadataFile(param.repositoryFolder())));
    }

    @Override
    public void putLongMetaData(PutMetadataParam param) {
        PropertyFileUtils.put(param.key(), param.value(), getMetadataFile(param.repositoryFolder()));
    }

    private File getMetadataFile(File repositoryFolder) {
        return new File(repositoryFolder, "metadata.properties");
    }
}
