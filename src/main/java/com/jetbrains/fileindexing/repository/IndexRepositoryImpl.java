package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.lexer.Lexer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class IndexRepositoryImpl implements IndexRepository {

    private final Map<String, String> indexStore;
    private final Lexer lexer;

    public IndexRepositoryImpl(final Lexer lexer) {
        this.lexer = lexer;
        this.indexStore = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> search(final String term) {
        final List<String> searchTokens = lexer.tokenize(term.toLowerCase());
        return indexStore.entrySet().stream()
                .filter(entry -> {
                    List<String> indexedTokens = lexer.tokenize(entry.getValue().toLowerCase());
                    return containsPhrase(indexedTokens, searchTokens);
                })
                .map(Map.Entry::getKey)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean containsPhrase(final List<String> indexedTokens, final List<String> searchTokens) {
        if (searchTokens.isEmpty()) {
            return false;
        }

        final int searchSize = searchTokens.size();
        for (int i = 0; i <= indexedTokens.size() - searchSize; i++) {
            if (indexedTokens.subList(i, i + searchSize).equals(searchTokens)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void putIndex(final String key, final String value) {
        indexStore.put(key, value);
    }

    @Override
    public void removeIndex(final String key) {
        indexStore.remove(key);
    }
}
