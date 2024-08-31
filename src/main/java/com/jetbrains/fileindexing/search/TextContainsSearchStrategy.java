package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.repository.MetadataRepository;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
public class TextContainsSearchStrategy implements SearchStrategy {

    private final File dataFolder;
    private final IndexRepository indexRepository = FactoryContainer.instance().indexRepository();
    private final MetadataRepository metadataRepository = FactoryContainer.instance().metadataRepository();

    @Override
    public void putIndex(String key, String value) {

    }

    @Override
    public List<String> search(String term) {
        return null;
    }

    @Override
    public void removeIndex(String key) {

    }
}
