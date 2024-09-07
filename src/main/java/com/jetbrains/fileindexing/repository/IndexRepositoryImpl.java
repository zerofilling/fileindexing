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

        Map<String, Set<Integer>> resultMap = null;
        for (int i = 0; i < searchTokens.size(); ++i) {
            // Get next token
            String token = searchTokens.get(i);
            // Retrieve (key -> Set<indices>) map for token. This map represents in which key token appears on which indexes
            Map<String, Set<Integer>> keyIndexMap = tensor3D.getKeyIndexKeyMap(token);
            if (resultMap != null) {
                // Iterating through first iteration's initialized map and remove on the fly the key in the case when
                // indexes for the next token does not contain one of index+i of
                for (Iterator<Map.Entry<String, Set<Integer>>> iterator = resultMap.entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, Set<Integer>> indexKeyEntry = iterator.next();
                    String resultKey = indexKeyEntry.getKey();
                    Set<Integer> resultIndices = indexKeyEntry.getValue();
                    Set<Integer> newIndexes = keyIndexMap.get(resultKey);
                    if (newIndexes != null) {
                        int finalI = i;
                        if (resultIndices.stream().noneMatch(it -> newIndexes.contains(it + finalI))) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
            } else {
                if (keyIndexMap == null) {
                    return Collections.emptyList();
                }
                // At first iteration initializing first token's appears keys for feature filtering
                resultMap = new HashMap<>(keyIndexMap);
            }
        }
        return new ArrayList<>(resultMap.keySet());
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
