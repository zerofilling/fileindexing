package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public interface FileSystemListener {
    void listenFilesChanges(List<File> watchingFiles, Consumer<File> createdOrUpdate, Consumer<File> deleted);
}
