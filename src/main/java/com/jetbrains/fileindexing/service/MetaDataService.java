package com.jetbrains.fileindexing.service;

import java.io.File;

public interface MetaDataService {
    Long getLastUpdateTime(File dataFolder);

    void updateLastTime(File dataFolder);
}
