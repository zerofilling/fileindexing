package com.jetbrains.fileindexing.search;

import java.io.File;
import java.util.List;

public interface Indexing {
    void indexAll(List<File> watchingFolder, SearchStrategy searchStrategy);

    void putIndex(File file, SearchStrategy searchStrategy);

    void removeIndex(File file, SearchStrategy searchStrategy);

    List<File> search(String term, SearchStrategy searchStrategy);
}
