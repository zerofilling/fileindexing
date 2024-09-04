package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.utils.TextFileFinder;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class IndexingImpl implements Indexing {

    private final SearchStrategy searchStrategy;

    public IndexingImpl(final SearchStrategy searchStrategy) {
        assert searchStrategy != null;
        this.searchStrategy = searchStrategy;
    }

    @Override
    public void indexAll(final List<File> watchingFolders) {
        TextFileFinder.findTextModifiedFiles(watchingFolders, this::indexFile);
    }

    @SneakyThrows
    @Override
    public void indexFile(final File file) {
        searchStrategy.putIndex(file.getAbsolutePath(), FileUtils.readFileToString(file, StandardCharsets.UTF_8));
    }

    @Override
    public void removeIndex(final File file) {
        searchStrategy.removeIndex(file.getAbsolutePath());
    }

    @Override
    public List<File> search(final String term) {
        return searchStrategy.search(term).stream().map(File::new).collect(Collectors.toList());
    }
}
