package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.function.Consumer;

public interface FileSystemListener {
    void listenFilesChanges(File watchingFolder, Consumer<File> createdOrUpdate, Consumer<File> deleted);
}
