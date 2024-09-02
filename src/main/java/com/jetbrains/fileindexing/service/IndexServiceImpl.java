package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.IndexRepository;
import com.jetbrains.fileindexing.utils.DatabaseCrashedException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    public IndexServiceImpl(String dbFilePath) {
        indexRepository = FactoryContainer.beansAbstractFactory().indexRepository(dbFilePath);
    }

    @Override
    public List<String> search(String term) {
        try {
            return indexRepository.search(term);
        } catch (SQLException e) {
            log.error("", e);
            throw new DatabaseCrashedException(e);
        }
    }

    @Override
    public void putIndex(String key, String value) {
        try {
            indexRepository.putIndex(key, value);
        } catch (SQLException e) {
            log.error("", e);
            throw new DatabaseCrashedException(e);
        }
    }

    @Override
    public void removeIndex(String key) {
        try {
            indexRepository.removeIndex(key);
        } catch (SQLException e) {
            log.error("", e);
            throw new DatabaseCrashedException(e);
        }
    }
}
