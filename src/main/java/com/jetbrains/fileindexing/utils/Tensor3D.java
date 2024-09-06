package com.jetbrains.fileindexing.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
                .computeIfAbsent(key, k -> new HashSet<>())
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
     * Retrieves a map of keys to sets of indices for a specific token.
     *
     * @param token The token whose key-index mappings are to be retrieved.
     * @return A map where each key is associated with a set of indices for the given token. If the token does not exist, an empty map is returned.
     */
    public Map<String, Set<Integer>> getKeyIndexKeyMap(String token) {
        return tokenToKeyIndexMap.get(token);
    }
}
