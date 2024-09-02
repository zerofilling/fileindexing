package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

/**
 * The {@code FileSystemListener} interface defines the contract for monitoring changes
 * in the file system. Implementations of this interface provide methods to listen for
 * file creation, modification, and deletion events within specified directories.
 */
public interface FileSystemListener {

    /**
     * Listens for changes in the specified list of files or directories.
     * It triggers the provided consumers when a file is created, updated, or deleted.
     *
     * @param watchingFiles  the list of files or directories to monitor
     * @param createdOrUpdate a consumer that handles file creation or update events
     * @param deleted        a consumer that handles file deletion events
     */
    void listenFilesChanges(List<File> watchingFiles, Consumer<File> createdOrUpdate, Consumer<File> deleted);
}
