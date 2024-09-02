package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import lombok.SneakyThrows;

import java.util.List;

public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    public IndexServiceImpl(String dbFilePath) {
        indexRepository = FactoryContainer.beansAbstractFactory().indexRepository(dbFilePath);
    }

    @SneakyThrows //  todo throw custom service exception
    @Override
    public List<String> search(String term) {
        return indexRepository.search(term);
    }

    @SneakyThrows //  todo throw custom service exception
    @Override
    public void putIndex(String key, String value) {
        indexRepository.putIndex(key, value);
    }

    @SneakyThrows //  todo throw custom service exception
    @Override
    public void removeIndex(String key) {
        indexRepository.removeIndex(key);
    }
}
