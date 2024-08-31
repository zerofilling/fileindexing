package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class FileVisitor {
    public static void visit(File[] files, Consumer<File> consume, Predicate<File> predicate) {
        for (File file : files) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children != null) {
                    visit(children, consume, predicate);
                }
            }
            if (predicate.test(file)) {
                consume.accept(file);
            }
        }
    }
}
