package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    public IndexServiceImpl(final Lexer lexer) {
        indexRepository = FactoryContainer.beansAbstractFactory().indexRepository(lexer);
    }

    @Override
    public List<String> search(final String term) {
        return indexRepository.search(term);
    }

    @Override
    public void putIndex(final String key, final String value) {
        indexRepository.putIndex(key, value);
    }

    @Override
    public void removeIndex(final String key) {
        indexRepository.removeIndex(key);
    }
}
