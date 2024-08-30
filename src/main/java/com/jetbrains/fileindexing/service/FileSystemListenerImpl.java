package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.function.Consumer;

public class FileSystemListenerImpl implements FileSystemListener {
    @Override
    public void listenFilesChanges(File watchingFolder, Consumer<File> createdOrUpdate, Consumer<File> deleted) {

    }
}
