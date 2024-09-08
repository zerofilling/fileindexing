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
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSearchTest {

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
                                    .watchingFolders(Lists.newArrayList(watchingFolder))
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
    void testSearching() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
//        List<File> result = fileSearch.search("interface");
//        assertEquals(result.size(), 3);

        List<File> result = fileSearch.search("A B C D");
        assertEquals(result.size(), 0);

        result = fileSearch.search("A D C E");
        assertEquals(result.size(), 1);
        Set<String> file = Set.of("file.txt");
        assertEquals(result.stream().filter(it -> file.contains(it.getName())).toList().size(), 1);

        result = fileSearch.search("one two four");
        assertEquals(result.size(), 0);

        result = fileSearch.search("class IndexingImpl");
        assertEquals(result.size(), 1);

        result = fileSearch.search("test1 test2 test3 test4");
        assertEquals(result.size(), 2);
        Set<String> p1p2 = Set.of("p1.txt", "p2.txt");
        assertEquals(result.stream().filter(it -> p1p2.contains(it.getName())).toList().size(), 2);

        result = fileSearch.search("test1 test3 test4");
        assertEquals(result.size(), 2);
        Set<String> p2p3 = Set.of("p2.txt", "p3.txt");
        assertEquals(result.stream().filter(it -> p2p3.contains(it.getName())).toList().size(), 2);

        result = fileSearch.search("test1 test4 test4");
        assertEquals(result.size(), 0);

    }

    @Test
    @Timeout(value = 5000)
    void testCheckDeleteFile() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File filToDelete = new File(watchingFolder, "1/1.txt");
        filToDelete.delete();
        try {
            // todo wait on indexes count is changed
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 2);
    }

    @Test
    @Timeout(value = 5000)
    void testCheckDeleteFolder() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File deleteFolder = new File(watchingFolder, "1/3");
        try {
            FileUtils.deleteDirectory(deleteFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            // todo wait on indexes count is changed
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 2);
    }

    @Test
    @Timeout(value = 5000)
    void testCheckCreateFile() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File fileToCopy = new File(watchingFolder, "1/1.txt");
        try {
            FileUtils.copyFile(fileToCopy, new File(fileToCopy.getParentFile(), "1-copy.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            // todo wait on indexes count is changed
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 4);
    }

    @Test
    @Timeout(value = 5000)
    void testCheckCreateFolder() {
        while (indexingStatusService.statusIs(Status.INDEXING)) {
            // wait for init indexes
        }
        List<File> result = fileSearch.search("interface");
        assertEquals(result.size(), 3);
        File folderToCopy = new File(watchingFolder, "1/3");
        try {
            FileUtils.copyDirectory(folderToCopy, new File(folderToCopy.getParentFile(), "1/4"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            // todo wait on indexes count is changed
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = fileSearch.search("interface");
        assertEquals(result.size(), 4);
    }
}
