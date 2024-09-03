package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import com.jetbrains.fileindexing.utils.Status;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSearchTest {

    private FileSearch fileSearch;
    private File dataDir;
    private File watchingFolder;

    @BeforeEach
    void init() {
        try {
            dataDir = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
            File dataFolder = new File(dataDir, "data");
            watchingFolder = new File(dataDir, "watchingFolder");
            FileUtils.copyDirectory(new File("src/test/resources/testdata"), watchingFolder);
            fileSearch = FileSearch.builder().config(
                            Config.builder()
                                    .searchStrategy(new TextContainsSearchStrategy(
                                            dataFolder))
                                    .watchingFolders(Lists.newArrayList(watchingFolder))
                                    .build())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @AfterEach
//    void destroy() {
//        try {
//            FileUtils.deleteDirectory(dataDir);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    void testSearching() {
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);

        result = fileSearch.search("class IndexingImpl");
        assertEquals(result.size(), 1);
    }

    @Test
    @Timeout(value = 5000, unit = TimeUnit.SECONDS)
    void testCheckDeleteFile() {
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // wait for init indexes
        }
        File filToDelete = new File(watchingFolder, "1/1.txt");
        filToDelete.delete();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 2);
    }
}
