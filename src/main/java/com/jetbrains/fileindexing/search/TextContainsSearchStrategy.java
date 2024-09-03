package com.jetbrains.fileindexing.search;

import com.jetbrains.fileindexing.config.FactoryContainer;
import com.jetbrains.fileindexing.lexer.DefaultLexer;
import com.jetbrains.fileindexing.lexer.Lexer;
import com.jetbrains.fileindexing.service.IndexService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TextContainsSearchStrategy implements SearchStrategy {

    private final IndexService indexService;

    public TextContainsSearchStrategy() {
        indexService = FactoryContainer.beansAbstractFactory().indexService(lexer());
    }

    @Override
    public void putIndex(String key, String value) {
        log.info("Put index '{}'", key);
        indexService.putIndex(key, value);
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
    }

    @Override
    public Lexer lexer() {
        return new DefaultLexer();
    }
}
