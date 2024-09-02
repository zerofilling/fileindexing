package com.jetbrains.fileindexing.utils;

/**
 * The {@code DatabaseCrashedException} class represents an exception that is thrown
 * when there is a critical failure in the index database, such as when the database file
 * is deleted or becomes inaccessible.
 * <p>
 * The purpose of this exception is to handle errors related to the index database.
 * For example, if someone deletes the index database file, the system will catch this exception,
 * create a new database file, and then reindex all files to restore the system's state.
 * </p>
 */
public class DatabaseCrashedException extends RuntimeException {

    /**
     * Constructs a new {@code DatabaseCrashedException} with the specified cause.
     *
     * @param cause the underlying reason for the exception (usually another exception)
     */
    public DatabaseCrashedException(Throwable cause) {
        super(cause);
    }
}
