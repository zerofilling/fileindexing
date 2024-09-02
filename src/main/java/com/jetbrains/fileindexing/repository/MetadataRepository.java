package com.jetbrains.fileindexing.repository;

import java.sql.SQLException;
import java.util.Optional;

public interface MetadataRepository {
    Optional<Long> getLongMetaData(String key) throws SQLException;

    void putLongMetaData(String key, Long value) throws SQLException;
}
