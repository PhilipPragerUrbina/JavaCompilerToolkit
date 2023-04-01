package Lexicographer.FrontEnd.Lexer;

import Lexicographer.IO.OutFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class LexerGeneratorTest {

    Lexer lexer_instance = null;

    //Get the generated lexer instance to test
    @BeforeEach
    void setUp(@TempDir(cleanup = CleanupMode.ON_SUCCESS) File temporary_directory) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Token configuration for tests
        ArrayList<TokenSpecification> specifications = new ArrayList<>();
        specifications.add(new TokenSpecification("\\\"([^\\\"]+)\\\"", 0,false, "string")); //Good for testing the automatic escaping of quotation marks
        specifications.add(new TokenSpecification("\\/\\/.*\\n", -1,true, "comment")); //Test discard
        specifications.add(new TokenSpecification("\\+", 2,false, "plus")); //Test priority
        specifications.add(new TokenSpecification("\\+\\+", 1,false, "plusplus")); //Test priority
        specifications.add(new TokenSpecification("(?<!\\w)(foo|bar)(?!\\w)", 1,false, "foo_or_bar")); //Test a word

        //Generate code
        LexerGenerator generator = new LexerGenerator(specifications, "test", "test");
        String source = generator.getCode();

        //Put code in temporary directory.
        File package_dir = new File(temporary_directory, "test");
        package_dir.mkdir();
        File source_file = new File(package_dir, "testLexer.java");
        OutFile out_source_file = new OutFile(source_file);
        out_source_file.writeText(source);

        //Compile code
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertEquals(compiler.run(null,null,null,source_file.getPath()), 0, "Generated Java code does not compile.");

        //Load class
        URLClassLoader class_loader = URLClassLoader.newInstance(new URL[] { temporary_directory.toURI().toURL() });
        Class<?> class_object = Class.forName("test.testLexer", true, class_loader);
        lexer_instance = (Lexer) class_object.getDeclaredConstructor().newInstance();
    }

    @Test
    void TestLexer(){
        assertNotNull (lexer_instance, "Lexer was not generated properly");

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