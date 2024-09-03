package com.jetbrains.fileindexing.lexer;

import java.util.List;

public interface Lexer {
    List<String> tokenize(String text);
}
