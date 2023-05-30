package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InterpretedLexerTest {

    @Test
    void tokenize() {
        //Token configuration for tests
        ArrayList<TokenSpecification> specifications = new ArrayList<>();
        specifications.add(new TokenSpecification("\\\"([^\\\"]+)\\\"", 0,false, "string")); //Good for testing the automatic escaping of quotation marks
        specifications.add(new TokenSpecification("\\/\\/.*\\n", -1,true, "comment")); //Test discard
        specifications.add(new TokenSpecification("\\+", 2,false, "plus")); //Test priority
        specifications.add(new TokenSpecification("\\+\\+", 1,false, "plusplus")); //Test priority
        specifications.add(new TokenSpecification("(?<!\\w)(foo|bar)(?!\\w)", 1,false, "foo_or_bar")); //Test a word

        InterpretedLexer lexer_instance = new InterpretedLexer(new LexerSpecification(specifications));

        ArrayList<Token> tokens = lexer_instance.tokenize(" \"foobar'\"+\"++instring\"\n+++foobarlkaj bar foo \n//foo \n"); //Test case with some pitfalls
        //Has multiple strings, other tokens within tokens, tokens right after each other, to name a few.
        assertArrayEquals(new Token[]{
                new Token("string", "foobar'",1),
                new Token("plus", null, 10),
                new Token("string", "++instring", 11),
                new Token("plusplus",null,24),
                new Token("plus", null, 26),
                //foo_or_bar is defined as needing to have non-word chars before and after, so no token here.
                new Token("foo_or_bar", "bar", 38),
                new Token("foo_or_bar", "foo", 42)
                //Comment is discard, so nothing here.
        },tokens.toArray(), "Tokens not detected properly");
    }
}