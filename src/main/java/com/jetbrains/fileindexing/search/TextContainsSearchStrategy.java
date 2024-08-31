package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.service.MetaDataService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
public class TextContainsSearchStrategy implements SearchStrategy {

    private final File dataFolder;
    private final IndexRepository indexRepository = FactoryContainer.instance().indexRepository();
    private final MetaDataService metaDataService = FactoryContainer.instance().metadataService();

    @Override
    public void putIndex(String key, String value) {
        putIndexedTime();
    }

    @Override
    public List<String> search(String term) {
        return null;
    }

    @Override
    public void removeIndex(String key) {
        putIndexedTime();
    }

    @Override
    public long getIndexedTime() {
        return metaDataService.getLastUpdateTime(dataFolder);
    }

    @Override
    public void putIndexedTime() {
        metaDataService.updateLastTime(dataFolder);
    }
}
