package com.jetbrains.fileindexing.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Consumer;

public class TextFileFinder {
    public static void findTextFiles(File directory, Consumer<File> fileProcessor) {
        Collection<File> files = FileUtils.listFiles(directory, null, true);
        for (File file : files) {
            if (isTextFile(file)) {
                fileProcessor.accept(file);
            }
        }
    }

    private static boolean isTextFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1000];
            int readBytes = IOUtils.read(fis, buffer);
            String content = new String(buffer, 0, readBytes, StandardCharsets.UTF_8);
            return content.chars().allMatch(ch -> ch == 9 || ch == 10 || ch == 13 || (ch >= 32 && ch <= 126) || (ch >= 128 && ch <= 255));
        } catch (IOException e) {
            return false;
        }
    }
}