package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.param.GetMetadataParam;
import com.jetbrains.fileindexing.param.PutMetadataParam;

public interface MetadataRepository {
    Long getLongMetaData(GetMetadataParam param);

    void putLongMetaData(PutMetadataParam param);
}
