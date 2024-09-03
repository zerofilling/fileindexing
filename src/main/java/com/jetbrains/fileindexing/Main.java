package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import com.jetbrains.fileindexing.utils.Status;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        FileSearch fileSearch = FileSearch.builder().config(
                        Config.builder()
                                .searchStrategy(new TextContainsSearchStrategy())
                                .watchingFolders(Lists.newArrayList(new File("/Users/david/dev/fileindexing/src/test/resources")))
                                .build())
                .build();
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        int size = result.size();
        System.out.println();
    }
}