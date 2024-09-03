package com.jetbrains.fileindexing.lexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultLexer implements Lexer {

    @Override
    public List<String> tokenize(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}