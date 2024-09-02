package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * The {@code TextFileFinder} class provides utility methods for finding and processing text files
 * within a list of files and directories. It includes methods to determine if a file has been modified,
 * whether a file is a text file, and to recursively visit and process files that meet certain criteria.
 */
public final class TextFileFinder {

    /**
     * Finds files that have been modified since the last update time and applies the specified consumer
     * action to each file.
     *
     * @param lastUpdatedTime the timestamp of the last update
     * @param files           the list of files and directories to search
     * @param consume         the consumer action to apply to each modified file
     */
    public static void findTextModifiedFiles(Long lastUpdatedTime, List<File> files, Consumer<File> consume) {
        FileVisitor.visitFiles(files.toArray(value -> new File[files.size()]), consume, file -> shouldIndex(lastUpdatedTime, file));
    }

    /**
     * Determines whether a file should be indexed based on its modification time and type.
     *
     * @param lastUpdatedTime the timestamp of the last update
     * @param file            the file to check
     * @return {@code true} if the file should be indexed; {@code false} otherwise
     */
    private static boolean shouldIndex(Long lastUpdatedTime, File file) {
        return isFileChanged(lastUpdatedTime, file) && (file.isDirectory() || isTextFile(file));
    }

    /**
     * Checks if a file has been modified since the last update time.
     *
     * @param lastUpdatedTime the timestamp of the last update
     * @param file            the file to check
     * @return {@code true} if the file has been modified; {@code false} otherwise
     */
    private static boolean isFileChanged(Long lastUpdatedTime, File file) {
        return lastUpdatedTime < file.lastModified();
    }

    /**
     * Determines if a file is a text file by checking its content for valid UTF-8 encoding.
     *
     * @param file the file to check
     * @return {@code true} if the file is a text file; {@code false} otherwise
     */
    public static boolean isTextFile(File file) {
        if (file.isDirectory()) {
            return false;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1000];
            int readBytes = fis.read(buffer);
            if (readBytes == -1) {
                return false;
            }
            byte[] data = new byte[readBytes];
            System.arraycopy(buffer, 0, data, 0, readBytes);
            return isValidUTF8(data);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if the given byte array is valid UTF-8 encoded text.
     *
     * @param bytes the byte array to check
     * @return {@code true} if the byte array is valid UTF-8; {@code false} otherwise
     */
    private static boolean isValidUTF8(byte[] bytes) {
        try {
            new String(bytes, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
