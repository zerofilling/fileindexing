package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class IndexingImpl implements Indexing {

    @Override
    public void indexAll(List<File> watchingFolders, SearchStrategy searchStrategy) {
        TextFileFinder.findTextModifiedFiles(watchingFolders, file -> putIndex(file, searchStrategy));
    }

    @SneakyThrows
    @Override
    public void putIndex(File file, SearchStrategy searchStrategy) {
        searchStrategy.putIndex(file.getAbsolutePath(), FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    }

    @Override
    public void removeIndex(File file, SearchStrategy searchStrategy) {
        searchStrategy.removeIndex(file.getAbsolutePath());
    }

    @Override
    public List<File> search(String term, SearchStrategy searchStrategy) {
        return searchStrategy.search(term).stream().map(File::new).collect(Collectors.toList());
    }
}
