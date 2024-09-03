package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.repository.DbInit;
import com.jetbrains.fileindexing.service.IndexService;
import com.jetbrains.fileindexing.service.MetaDataService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class TextContainsSearchStrategy implements SearchStrategy {

    private final File dataFolder;
    private final IndexService indexService;
    private final MetaDataService metaDataService;
    private final String dbFilePath;

    public TextContainsSearchStrategy(File dataFolder) {
        dataFolder.mkdirs();
        this.dataFolder = dataFolder;
        this.dbFilePath = dataFolder + File.separator + "fileindex.db";
        DbInit.init(dbFilePath);
        indexService = FactoryContainer.beansAbstractFactory().indexService(dbFilePath);
        metaDataService = FactoryContainer.beansAbstractFactory().metadataService(dbFilePath);
    }

    @Override
    public void putIndex(String key, String value) {
        log.info("Put index '{}'", key);
        indexService.putIndex(key, value);
        putIndexedTime();
    }

    @Override
    public List<String> search(String term) {
        log.info("Search index, term: '{}'", term);
        return indexService.search(term);
    }

    @Override
    public void removeIndex(String key) {
        log.info("Remove index '{}'", key);
        indexService.removeIndex(key);
        putIndexedTime();
    }

    @Override
    public long getIndexedTime() {
        return metaDataService.getLastUpdateTime(dataFolder);
    }

    @Override
    public void putIndexedTime() {
        metaDataService.updateLastTime(dataFolder);
    }

    @Override
    public void cleanDb() {
        new File(dbFilePath).delete();
        DbInit.init(dbFilePath);
    }
}
