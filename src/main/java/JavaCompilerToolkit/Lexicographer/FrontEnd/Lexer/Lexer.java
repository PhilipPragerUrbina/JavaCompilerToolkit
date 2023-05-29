package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import java.util.ArrayList;

/**
 * A tokenizer
 */
public interface Lexer {

    /**
     * Take a file and extract tokens
     * @param file File contents
     * @return List of tokens in order
     */
    ArrayList<Token> tokenize(String file);

}
