package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;

import java.util.List;

public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository = FactoryContainer.instance().indexRepository();

    @Override
    public List<String> search(String term, String dbFilePath) {
        return indexRepository.search(term, dbFilePath);
    }

    @Override
    public void putIndex(String key, String value, String dbFilePath) {
        indexRepository.putIndex(key, value, dbFilePath);
    }

    @Override
    public void removeIndex(String key, String dbFilePath) {
        indexRepository.removeIndex(key, dbFilePath);
    }
}
