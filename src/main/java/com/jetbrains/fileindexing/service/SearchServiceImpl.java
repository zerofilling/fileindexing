package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {
    @Override
    public List<File> search(String term, SearchStrategy searchStrategy) {
        return searchStrategy.search(term).stream().map(File::new).collect(Collectors.toList());
    }
}
