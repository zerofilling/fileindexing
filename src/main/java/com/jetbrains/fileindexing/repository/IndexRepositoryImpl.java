package com.jetbrains.fileindexing.repository;

import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.utils.LoopWithIndexConsumer;
import com.jetbrains.fileindexing.utils.Tensor3D;

import java.util.*;

public class IndexRepositoryImpl implements IndexRepository {

    private final Tensor3D tensor3D;

    private final Lexer lexer;

    public IndexRepositoryImpl(final Lexer lexer) {
        this.lexer = lexer;
        this.tensor3D = new Tensor3D();
    }

    @Override
    public List<String> search(final String term) {
        final List<String> searchTokens = lexer.tokenize(term);

        if (searchTokens.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Set<Integer>> firstTokenMap = null;
        Map<String, Set<Integer>> prevTokenMap = null;
        for (String token: searchTokens) {
            // Retrieve (key -> Set<indices>) map for token. This map represents in which key token appears on which indexes
            if (firstTokenMap != null) {
                // Iterating through first iteration's initialized map and remove on the fly the key in the case when
                // indexes for the next token does not contain one of index+i of
                for (Iterator<Map.Entry<String, Set<Integer>>> iterator = firstTokenMap.entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, Set<Integer>> indexKeyEntry = iterator.next();
                    String filePath = indexKeyEntry.getKey();
                    Set<Integer> newIndexes = tensor3D.getIndexes(token, filePath);
                    if (newIndexes != null) {
                        Set<Integer> prevTokenIndices = prevTokenMap.get(filePath);
                        if (prevTokenIndices.stream().noneMatch(it -> newIndexes.contains(it + 1))) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
                // duplicating map to have no chance to change it here.
                prevTokenMap = tensor3D.duplicateTokenMap(token);
            } else {
                if (!tensor3D.tokenExists(token)) {
                    return Collections.emptyList();
                }
                // At first iteration initializing first token's appears keys for feature filtering
                firstTokenMap = tensor3D.duplicateTokenMap(token);
                prevTokenMap = firstTokenMap;
            }
            if(prevTokenMap == null) {
                return Collections.emptyList();
            }
        }
        return new ArrayList<>(firstTokenMap.keySet());
    }

    @Override
    public void putIndex(final String key, final String value) {
        final List<String> tokens = lexer.tokenize(value);
        LoopWithIndexConsumer.forEach(tokens, (token, index) -> tensor3D.add(key, token, index));
    }

    @Override
    public void removeIndex(final String key) {
        tensor3D.removeKey(key);
    }
}
