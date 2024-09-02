package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The {@code FileVisitor} class provides a utility method to recursively visit files and directories,
 * applying a specified action to each file that meets certain criteria.
 * <p>
 * This class is designed to facilitate operations on file systems, such as processing files that
 * meet specific conditions or traversing directory structures.
 * </p>
 */
public final class FileVisitor {

    /**
     * Recursively visits files and directories, applying the specified consumer action to each file
     * that satisfies the given predicate.
     *
     * @param files       an array of {@link File} objects to be visited
     * @param consume     a {@link Consumer} that performs an action on each file that meets the criteria
     * @param shouldVisit a {@link Predicate} that determines whether a file should be visited
     */
    public static void visitFiles(File[] files, Consumer<File> consume, Predicate<File> shouldVisit) {
        for (File file : files) {
            if (shouldVisit.test(file)) {
                if (file.isDirectory()) {
                    File[] children = file.listFiles();
                    if (children != null) {
                        visitFiles(children, consume, shouldVisit);
                    }
                } else {
                    consume.accept(file);
                }
            }
        }
    }
}
