package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        FileSearch fileSearch = FileSearch.builder().config(
                        Config.builder()
                                .searchStrategy(new TextContainsSearchStrategy(
                                        new File("/Users/david/dev/fileindexing/.searchdata")))
                                .watchingFolders(Lists.newArrayList(new File("/Users/david/dev/fileindexing/src/test/resources")))
                                .build())
                .build();
        while(true) {
            try {
                List<File> result = fileSearch.search("fsd");
                log.info(result.toString());
                break;
            } catch (Exception e) {
//                log.info("search not ready");
            }
        }

    }
}