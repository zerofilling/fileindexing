package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;

import java.util.List;

public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository = FactoryContainer.instance().indexRepository();

    @Override
    public List<String> search(String term) {
        return indexRepository.search(term);
    }

    @Override
    public void putIndex(String key, String value) {

    }

    @Override
    public void removeIndex(String key) {

    }
}
