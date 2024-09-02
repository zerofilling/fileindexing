package com.jetbrains.fileindexing.utils;

public class DatabaseCrashedException extends RuntimeException {
    public DatabaseCrashedException(Throwable cause) {
        super(cause);
    }
}
