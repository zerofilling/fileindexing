package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileSearchTest {
    @Test
    void testSearchWhenIndexing() throws InterruptedException {
        FileSearch fileSearch = FileSearch.builder().config(
                        Config.builder()
                                .searchStrategy(new TextContainsSearchStrategy(
//                                        new File("/Users/david/dev/fileindexing/.searchdata")))
                                        new File("/Users/david/dev/fileindexing/.searchdata")))
                                .watchingFolders(Lists.newArrayList(new File("/Users/david/dev/fileindexing/src/test/resources")))
                                .build())
                .build();


    }
}
