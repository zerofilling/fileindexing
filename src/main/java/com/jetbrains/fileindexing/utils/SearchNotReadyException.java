package com.jetbrains.fileindexing.utils;

/**
 * The {@code SearchNotReadyException} class represents an exception that is thrown
 * when a search operation is attempted before the indexing process has been completed.
 * <p>
 * This exception is used to signal that the search operation cannot proceed because
 * the indexing is still in progress or has not yet been initialized.
 * </p>
 */
public class SearchNotReadyException extends RuntimeException {

}
