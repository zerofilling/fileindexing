package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.search.SearchStrategy;

import java.io.File;
import java.util.List;

public interface SearchService {
    List<File> search(String term, SearchStrategy searchStrategy);
}
