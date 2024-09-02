package com.jetbrains.fileindexing.repository;

import java.sql.SQLException;
import java.util.Optional;

/**
 * The {@code MetadataRepository} interface defines the contract for storing and retrieving metadata
 * associated with the file indexing service. Implementations of this interface provide methods
 * to store and fetch metadata values.
 */
public interface MetadataRepository {

    /**
     * Retrieves a metadata value associated with the given key as a {@link Long}.
     *
     * @param key the metadata key
     * @return an {@link Optional} containing the metadata value, or empty if not found
     * @throws SQLException if a database access error occurs
     */
    Optional<Long> getLongMetaData(String key) throws SQLException;

    /**
     * Stores a metadata value associated with the given key.
     *
     * @param key   the metadata key
     * @param value the metadata value to be stored
     * @throws SQLException if a database access error occurs
     */
    void putLongMetaData(String key, Long value) throws SQLException;
}
