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

        final String firstToken = searchTokens.get(0);
        if (!tensor3D.tokenExists(firstToken)) {
            return Collections.emptyList();
        }
        final Map<String, Set<Integer>> firstTokenMap = tensor3D.getTokenMap(firstToken);
        for (final Iterator<Map.Entry<String, Set<Integer>>> iterator = firstTokenMap.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<String, Set<Integer>> keyIndexesEntry = iterator.next();
            final String firstTokenKey = keyIndexesEntry.getKey();
            final Set<Integer> firstTokenIndexes = keyIndexesEntry.getValue();

            boolean termMatchesKey = false;
            for (Integer firstIndex : firstTokenIndexes) {
                final boolean otherTokensValid = checkOtherTokens(searchTokens, firstTokenKey, firstIndex);
                if(otherTokensValid) {
                    termMatchesKey = true;
                    break;
                }
            }
            if (!termMatchesKey) {
                iterator.remove();
            }
        }
        return new ArrayList<>(firstTokenMap.keySet());
    }

    private boolean checkOtherTokens(final List<String> searchTokens, final String firstTokenKey, final Integer firstIndex) {
        for (int i = 1; i < searchTokens.size(); i++) {
            final String currentToken = searchTokens.get(i);
            if (!isTokenInIndex(firstTokenKey, currentToken, firstIndex + i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTokenInIndex(final String key, final String token, final int index) {
        final Map<String, Set<Integer>> keyMap = tensor3D.getTokenMap(token);
        if (keyMap == null) {
            return false;
        }
        final Set<Integer> indexes = keyMap.get(key);
        if (indexes == null) {
            return false;
        }
        return indexes.contains(index);
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
