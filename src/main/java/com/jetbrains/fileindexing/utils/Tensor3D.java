package com.jetbrains.fileindexing.utils;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a 3D tensor-like data structure that maps tokens to keys and indices.
 * <p>
 * This class allows for efficient storage and retrieval of indices associated with tokens and keys.
 * </p>
 */
public class Tensor3D {
    // token -> (key -> Set<indices>)
    private final Map<String, Map<String, Set<Integer>>> tokenToKeyIndexMap;

    /**
     * Constructs a new, empty Tensor3D instance.
     */
    public Tensor3D() {
        this.tokenToKeyIndexMap = new ConcurrentHashMap<>();
    }

    /**
     * Adds a new entry to the tensor.
     *
     * @param key   The key associated with the token.
     * @param token The token associated with the key.
     * @param index The index to be added.
     */
    public void add(String key, String token, Integer index) {
        tokenToKeyIndexMap.computeIfAbsent(token, t -> new ConcurrentHashMap<>())
                .computeIfAbsent(key, k -> Sets.newConcurrentHashSet())
                .add(index);
    }

    /**
     * Removes all indices associated with the given key.
     * <p>
     * This method removes the key from all token mappings.
     * </p>
     *
     * @param key The key to be removed.
     */
    public void removeKey(String key) {
        for (Map<String, Set<Integer>> keyIndexMap : tokenToKeyIndexMap.values()) {
            keyIndexMap.remove(key);
        }
    }

    /**
     * Retrieves the set of indices associated with the specified token and key.
     * <p>
     * This method returns a set of indices for the given token and key. If the token or key does not exist
     * in the tensor, the method returns {@code null}.
     * </p>
     *
     * @param token The token for which indices are to be retrieved.
     * @param key   The key associated with the token for which indices are to be retrieved.
     * @return A set of indices associated with the specified token and key, or {@code null} if either the
     * token or key does not exist in the tensor.
     */
    public Set<Integer> getIndices(String token, String key) {
        Map<String, Set<Integer>> stringSetMap = tokenToKeyIndexMap.get(token);
        if (stringSetMap != null) {
            Set<Integer> indices = stringSetMap.get(key);
            return indices == null ? null : Sets.newConcurrentHashSet(indices);
        }
        return null;
    }

    /**
     * Checks if the specified token exists in the tensor.
     * <p>
     * This method determines whether the given token has any associated keys and indices in the tensor.
     * It returns {@code true} if the token exists and has associated keys, otherwise {@code false}.
     * </p>
     *
     * @param token The token to check for existence.
     * @return {@code true} if the token exists and has associated keys, {@code false} otherwise.
     */
    public boolean tokenExists(String token) {
        Map<String, Set<Integer>> stringSetMap = tokenToKeyIndexMap.get(token);
        return stringSetMap != null && !stringSetMap.isEmpty();
    }

    /**
     * Creates a deep copy of the token-to-key index map for the specified token.
     * <p>
     * This method returns a new {@link HashMap} containing the indices associated with the specified token.
     * The returned map is a copy and does not affect the original tensor's state.
     * </p>
     *
     * @param token The token for which the index map is to be duplicated.
     * @return A {@link Map} containing the keys and associated indices for the specified token, or {@code null}
     * if the token does not exist in the tensor.
     */
    public Map<String, Set<Integer>> getTokenMap(String token) {
        Map<String, Set<Integer>> keyIndexesMap = tokenToKeyIndexMap.get(token);
        return keyIndexesMap == null
                ? null
                : keyIndexesMap.entrySet().stream().collect(Collectors.toConcurrentMap(Map.Entry::getKey,
                stringSetEntry -> Sets.newConcurrentHashSet(stringSetEntry.getValue())));
    }
}
