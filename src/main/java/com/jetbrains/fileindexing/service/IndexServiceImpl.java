package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    public IndexServiceImpl(Lexer lexer) {
        indexRepository = FactoryContainer.beansAbstractFactory().indexRepository(lexer);
    }

    @Override
    public List<String> search(String term) {
        return indexRepository.search(term);
    }

    @Override
    public void putIndex(String key, String value) {
        indexRepository.putIndex(key, value);
    }

    @Override
    public void removeIndex(String key) {
        indexRepository.removeIndex(key);
    }
}
