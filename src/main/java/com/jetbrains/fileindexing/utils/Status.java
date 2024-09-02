package com.jetbrains.fileindexing.utils;

/**
 * The {@code Status} enum represents the various states of the file indexing process.
 * <p>
 * This enum is used to track the progress and current state of indexing, allowing the system
 * to determine whether indexing is ongoing, completed successfully, or has failed.
 * </p>
 */
public enum Status {
    /**
     * Indicates that the indexing process is currently in progress.
     */
    INDEXING,

    /**
     * Indicates that the indexing process has completed successfully and the system is ready for searches.
     */
    READY,

    /**
     * Indicates that the indexing process has failed.
     */
    FAILED
}
