package com.jetbrains.fileindexing.service;

import java.io.File;

/**
 * The {@code MetaDataService} interface defines the contract for managing metadata related to file updates
 * within the file indexing system. Implementations of this interface provide methods to retrieve and update
 * the last modification time of a specified data folder.
 */
public interface MetaDataService {

    /**
     * Retrieves the last update time for the specified data folder.
     *
     * @param dataFolder the folder for which to retrieve the last update time
     * @return the last update time as a {@link Long}, or {@code null} if no update time is recorded
     */
    Long getLastUpdateTime(File dataFolder);

    /**
     * Updates the last modification time for the specified data folder to the current time.
     *
     * @param dataFolder the folder for which to update the last modification time
     */
    void updateLastTime(File dataFolder);
}
