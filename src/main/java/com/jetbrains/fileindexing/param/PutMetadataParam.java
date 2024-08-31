package com.jetbrains.fileindexing.param;

import java.io.File;

public record PutMetadataParam(String key, Object value, File repositoryFolder) {
}
