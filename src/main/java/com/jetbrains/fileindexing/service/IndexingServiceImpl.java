package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;
import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class IndexingServiceImpl implements IndexingService {


    @Override
    public void indexAll(List<File> watchingFiles, SearchStrategy searchStrategy) {
        long lastUpdatedTime = searchStrategy.getIndexedTime();
        TextFileFinder.findTextModifiedFiles(lastUpdatedTime, watchingFiles, file -> putIndex(file, searchStrategy));
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
}
