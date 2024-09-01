package com.jetbrains.fileindexing.repository;

import java.util.List;

public interface IndexRepository {
    List<String> search(String term);
}
