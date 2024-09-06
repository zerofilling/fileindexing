package com.jetbrains.fileindexing;

import com.google.common.collect.Lists;
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import com.jetbrains.fileindexing.service.IndexingStatusService;
import com.jetbrains.fileindexing.utils.Status;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleFileSearchTest {

    private static final IndexingStatusService indexingStatusService = FactoryContainer.beansAbstractFactory()
            .indexingStatusService();

    private static final List<File> watchingFolders = new ArrayList<>();
    private FileSearch fileSearch;
    private File watchingFolder;

    @BeforeEach
    void init() {
        try {
            watchingFolder = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
            watchingFolders.add(watchingFolder);
            FileUtils.copyDirectory(new File("src/test/resources/testdata"), watchingFolder);
            fileSearch = FileSearch.builder().config(
                            Config.builder()
                                    .searchStrategy(new TextContainsSearchStrategy())
                                    .watchingFolders(Lists.newArrayList(new File(watchingFolder, "1/1.txt")))
                                    .build())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void destroy() {
        watchingFolders.forEach(watchingFolder -> {
            try {
                FileUtils.deleteDirectory(watchingFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testSingleFileSearching() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("MetaDataService");
        assertEquals(result.size(), 1);

        result = fileSearch.search("class IndexingImpl");
        assertEquals(result.size(), 0);
    }

    @Test
    void testSingleFileChangeSearching() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("class IndexingImpl");
        assertEquals(result.size(), 0);
        try {
            File file = new File(watchingFolder, "1/1.txt");
            String existingContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            String newContent = existingContent + "class IndexingImpl";
            FileUtils.writeStringToFile(file, newContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            // todo wait on indexes count is changed
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("class IndexingImpl");
        assertEquals(result.size(), 1);

    }
}
