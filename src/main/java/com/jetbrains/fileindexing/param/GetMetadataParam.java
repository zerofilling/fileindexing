package com.jetbrains.fileindexing.param;

import java.io.File;

public record GetMetadataParam(String key, File repositoryFolder) {
}
