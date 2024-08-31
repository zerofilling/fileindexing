package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.search.SearchStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.List;

@Getter
@ToString
public final class Config {
    private final List<File> watchingFolders;
    private final SearchStrategy searchStrategy;

    @Builder
    private Config(List<File> watchingFolders, SearchStrategy searchStrategy) {
        assert watchingFolders != null;
        assert searchStrategy != null;
        this.watchingFolders = watchingFolders;
        this.searchStrategy = searchStrategy;
    }
}
