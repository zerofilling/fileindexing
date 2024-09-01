package com.jetbrains.fileindexing.service;

import java.io.File;
import java.util.function.Consumer;

public interface FileTaxonomyService {
    void addFile(File file);
    void delete(File file);
    void addFolder(File folder);
    void visitFiles(File folder, Consumer<File> child);

}
