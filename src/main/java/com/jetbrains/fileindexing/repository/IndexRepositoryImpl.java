package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.utils.LoopWithIndexConsumer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexRepositoryImpl implements IndexRepository {


    // key, token, index
    private final Map<String, Map<String, Set<Integer>>> keyTokenIndex;

    private final Lexer lexer;

    public IndexRepositoryImpl(final Lexer lexer) {
        this.lexer = lexer;
        this.keyTokenIndex = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> search(final String term) {
        final List<String> searchTokens = lexer.tokenize(term);

        if (searchTokens.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> results = new ArrayList<>();
        for (Map.Entry<String, Map<String, Set<Integer>>> entry : keyTokenIndex.entrySet()) {
            final String key = entry.getKey();
            final Map<String, Set<Integer>> tokenIndex = entry.getValue();

            Set<Integer> initialIndexes = tokenIndex.get(searchTokens.get(0));
            if (searchTokens.size() > 1) {
                boolean addResult = false;
                for (int i = 1; i < searchTokens.size(); ++i) {
                    String nextToken = searchTokens.get(i);
                    Set<Integer> nextIndexes = tokenIndex.get(nextToken);
                    if (nextIndexes != null) {
                        int finalI = i;
                        boolean present = nextIndexes.stream().anyMatch(it -> initialIndexes.contains(it - finalI));
                        if (present) {
                            addResult = true;
                        } else {
                            break;
                        }
                    }
                }
                if (addResult) {
                    results.add(key);
                }
            } else if (initialIndexes != null) {
                results.add(key);
            }
        }

        return new ArrayList<>(results);
    }

    @Override
    public void putIndex(final String key, final String value) {
        final List<String> tokens = lexer.tokenize(value);
        LoopWithIndexConsumer.forEach(tokens, (token, index) -> {
            final Map<String, Set<Integer>> indexKeyValkSetMap = keyTokenIndex.computeIfAbsent(key, s -> new HashMap<>());
            indexKeyValkSetMap.computeIfAbsent(token, s -> new HashSet<>());
            indexKeyValkSetMap.get(token).add(index);
        });
    }

    @Override
    public void removeIndex(final String key) {
        keyTokenIndex.remove(key);
    }
}
