package com.jetbrains.fileindexing.lexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A default implementation of the {@link Lexer} interface.
 * <p>
 * This class provides a basic tokenization strategy by splitting the input text
 * based on whitespace characters. Each token is converted to lowercase.
 * </p>
 */
public class DefaultLexer implements Lexer {

    /**
     * Tokenizes the given text into a list of tokens.
     * <p>
     * This implementation splits the text using whitespace characters as delimiters,
     * and converts each token to lowercase. This results in a list of words derived from
     * the input text.
     * </p>
     *
     * @param text the text to tokenize
     * @return a list of tokens extracted from the text, with each token in lowercase
     */
    @Override
    public List<String> tokenize(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
