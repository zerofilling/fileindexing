package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.function.Consumer;

/**
 * The {@code FileTaxonomyService} interface defines the contract for managing files and directories
 * within the file indexing system. Implementations of this interface provide methods to add and delete
 * files and folders, as well as to traverse files within a directory.
 * <p>
 * The purpose of this taxonomy is to maintain a structure of files and folders, allowing the system to track
 * the relationships between parent directories and their child files. This is essential because if a directory
 * containing child files is deleted, there would otherwise be no way to identify and remove the corresponding
 * indexes for those child files. By maintaining this taxonomy, the system can properly manage and clean up
 * file indexes, even when entire directories are removed.
 * </p>
 */
public interface FileTaxonomyService {

    /**
     * Adds a file to the file taxonomy.
     *
     * @param file the file to be added
     */
    void addFile(File file);

    /**
     * Deletes a file from the file taxonomy.
     *
     * @param file the file to be deleted
     */
    void delete(File file);

    /**
     * Adds a folder to the file taxonomy.
     *
     * @param folder the folder to be added
     */
    void addFolder(File folder);

    /**
     * Visits all files in the specified folder, applying the provided consumer to each file.
     *
     * @param folder the folder whose files are to be visited
     * @param child  a consumer that processes each file within the folder
     */
    void visitFiles(File folder, Consumer<File> child);
}
