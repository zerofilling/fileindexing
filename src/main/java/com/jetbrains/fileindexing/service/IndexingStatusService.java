package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.utils.Status;

/**
 * The {@code IndexingStatusService} interface provides methods to manage and query the status of indexing operations.
 * Implementations of this interface handle the lifecycle and status reporting of indexing processes.
 */
public interface IndexingStatusService {

    /**
     * Starts the indexing process. This method should be called when the indexing operation begins.
     */
    void start();

    /**
     * Marks the indexing process as successful. This method should be called when the indexing operation completes
     * successfully.
     */
    void success();

    /**
     * Marks the indexing process as failed. This method should be called when the indexing operation encounters an
     * error or fails to complete successfully.
     */
    void fail();

    /**
     * Retrieves the current status of the indexing process.
     *
     * @return the current {@link Status} of the indexing process, indicating whether it is in progress, successful,
     *         or failed.
     */
    Status getStatus();

    /**
     * Checks if the current status of the indexing process matches the specified status.
     *
     * @param indexing the {@link Status} to compare against the current status of the indexing process.
     * @return {@code true} if the current status matches the specified status, {@code false} otherwise.
     */
    boolean statusIs(Status indexing);
}
