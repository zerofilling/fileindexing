package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

public final class TextFileFinder {

    public static void findTextModifiedFiles(Long lastUpdatedTime, List<File> files, Consumer<File> consume) {
        FileVisitor.visitFiles(files.toArray(value -> new File[files.size()]), consume, file -> shouldIndex(lastUpdatedTime, file));
    }

    private static boolean shouldIndex(Long lastUpdatedTime, File file) {
        return isFileChanged(lastUpdatedTime, file) && (file.isDirectory() || isTextFile(file));
    }

    private static boolean isFileChanged(Long lastUpdatedTime, File file) {
        return lastUpdatedTime < file.lastModified();
    }

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

    private static boolean isValidUTF8(byte[] bytes) {
        try {
            new String(bytes, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}