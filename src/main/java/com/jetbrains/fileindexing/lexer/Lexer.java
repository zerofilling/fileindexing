package com.jetbrains.fileindexing.lexer;

import java.util.List;

/**
 * Interface for tokenizing text.
 * <p>
 * Implementations of this interface should provide different strategies for breaking
 * down a string into a list of tokens. A token is typically a word or a meaningful
 * unit of text.
 * </p>
 */
public interface Lexer {

    /**
     * Tokenizes the given text into a list of tokens.
     * <p>
     * This method takes a string of text and returns a list of tokens derived from that
     * text. The exact tokenization strategy depends on the implementation of the Lexer.
     * </p>
     *
     * @param text the text to tokenize
     * @return a list of tokens extracted from the text
     */
    List<String> tokenize(String text);
}
