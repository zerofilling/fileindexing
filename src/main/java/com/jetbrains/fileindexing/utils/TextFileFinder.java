package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
     * @param watchingFolders the list of watchingFolders and directories to search
     * @param consume         the consumer action to apply to each modified file
     */
    public static void findTextModifiedFiles(List<File> watchingFolders, Consumer<File> consume) {
        watchingFolders.forEach(watchingFolder -> {
            try (Stream<Path> paths = Files.walk(watchingFolder.toPath())) {
                paths.map(Path::toFile).filter(File::isFile).filter(TextFileFinder::shouldIndex)
                        .forEach(consume);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Determines whether a file should be indexed based on its modification time and type.
     *
     * @param file            the file to check
     * @return {@code true} if the file should be indexed; {@code false} otherwise
     */
    private static boolean shouldIndex(File file) {
        return file.isDirectory() || isTextFile(file);
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
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPORT);
        decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }
}
