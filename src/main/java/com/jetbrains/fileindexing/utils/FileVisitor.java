package com.jetbrains.fileindexing.utils;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class FileVisitor {
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
