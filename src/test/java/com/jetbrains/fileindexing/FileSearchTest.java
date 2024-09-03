package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import com.jetbrains.fileindexing.utils.Status;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSearchTest {

    private static final List<File> dataDirs = new ArrayList<>();
    private FileSearch fileSearch;
    private File watchingFolder;

    @BeforeEach
    void init() {
        try {
            File dataDir = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
            dataDirs.add(dataDir);
            watchingFolder = new File(dataDir, "watchingFolder");
            FileUtils.copyDirectory(new File("src/test/resources/testdata"), watchingFolder);
            fileSearch = FileSearch.builder().config(
                            Config.builder()
                                    .searchStrategy(new TextContainsSearchStrategy())
                                    .watchingFolders(Lists.newArrayList(watchingFolder))
                                    .build())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void destroy() {
        dataDirs.forEach(dataDir -> {
            try {
                FileUtils.deleteDirectory(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

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
    @Timeout(value = 5000)
    void testCheckDeleteFile() {
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File filToDelete = new File(watchingFolder, "1/1.txt");
        filToDelete.delete();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 2);
    }

    @Test
    @Timeout(value = 5000)
    void testCheckCreateFile() {
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File filToCopy = new File(watchingFolder, "1/1.txt");
        try {
            FileUtils.copyFile(filToCopy, new File(filToCopy.getParentFile(), "1-copy.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 4);
    }
}
