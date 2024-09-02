package com.jetbrains.fileindexing.repository;

import java.sql.SQLException;

public interface MetadataRepository {
    Long getLongMetaData(String key) throws SQLException; // todo optional

    void putLongMetaData(String key, Long value) throws SQLException;
}
