package com.jetbrains.fileindexing.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class IndexRepositoryImpl implements IndexRepository {

    private final Map<String, String> indexStore;

    public IndexRepositoryImpl() {
        this.indexStore = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> search(String term) {
        String normalizedTerm = term.toLowerCase();
        return indexStore.entrySet().stream()
                .filter(entry -> entry.getValue().toLowerCase().contains(normalizedTerm))
                .map(Map.Entry::getKey)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void putIndex(String key, String value) {
        indexStore.put(key, value);
    }

    @Override
    public void removeIndex(String key) {
        indexStore.remove(key);
    }
}
