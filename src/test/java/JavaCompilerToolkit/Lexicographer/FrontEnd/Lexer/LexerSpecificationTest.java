package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import JavaCompilerToolkit.Lexicographer.IO.OutFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class LexerSpecificationTest {

    @TempDir(cleanup = CleanupMode.ON_SUCCESS) File temporary_directory;

    @Test
    void getSpecifications() {
        assertThrows(IOException.class,() -> {
           LexerSpecification invalid_capture = new LexerSpecification(new File("src/test/resources/LexerTestFiles/invalid_capture.jsonc"));
        });
        assertThrows(IOException.class,() -> {
            LexerSpecification invalid_token = new LexerSpecification(new File("src/test/resources/LexerTestFiles/invalid_token.jsonc"));
        });
        assertThrows(IOException.class,() -> {
            LexerSpecification invalid_token = new LexerSpecification(new File("src/test/resources/LexerTestFiles/invalid_token_2.jsonc"));
        });
        assertThrows(IOException.class,() -> {
            LexerSpecification invalid_top = new LexerSpecification(new File("src/test/resources/LexerTestFiles/invalid_top.jsonc"));
        });

      assertDoesNotThrow(() -> {
          LexerSpecification valid = new LexerSpecification(new File("src/test/resources/LexerTestFiles/valid.jsonc"));
      });
    }

    @Test
    void writeJson() throws IOException {
        //Token configuration for tests
        ArrayList<TokenSpecification> specifications = new ArrayList<>();
        specifications.add(new TokenSpecification("\\\"([^\\\"]+)\\\"", 0,false, "string")); //Good for testing the automatic escaping of quotation marks
        specifications.add(new TokenSpecification("\\/\\/.*\\n", -1,true, "comment")); //Test discard
        specifications.add(new TokenSpecification("\\+", 2,false, "plus")); //Test priority
        specifications.add(new TokenSpecification("\\+\\+", 1,false, "plusplus")); //Test priority
        specifications.add(new TokenSpecification("(?<!\\w)(foo|bar)(?!\\w)", 1,false, "foo_or_bar")); //Test a word
        LexerSpecification spec = new LexerSpecification(specifications);

        //Write file
        File file = new File(temporary_directory, "serialized.json");
        spec.writeJson(new OutFile(file));
        //Read file
        LexerSpecification other_spec = new LexerSpecification(file);
        //Get tokens
        ArrayList<TokenSpecification> original = spec.getSpecifications();
        ArrayList<TokenSpecification> after_read = other_spec.getSpecifications();
        //Order does not matter so put in consistent order
        original.sort(Comparator.comparingInt(TokenSpecification::getPriority));
        after_read.sort(Comparator.comparingInt(TokenSpecification::getPriority));
        assertEquals(original,after_read, "Deserialization does not match serialization");
    }
}