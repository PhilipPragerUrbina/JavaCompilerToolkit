package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.LexerSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.TokenSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.ParserSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParserSpecificationValidatorTest {

    @Test
    void validate() {
        ArrayList<TokenSpecification> tokens = new ArrayList<>();
        tokens.add(new TokenSpecification("",0,false,"foo"));
        tokens.add(new TokenSpecification("",0,false,"bar"));

        LexerSpecification lexer_specification = new LexerSpecification(tokens);

        assertThrows(ParserSpecificationValidator.ValidationException.class,() -> {
            ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/empty_node.jsonc"));
            new ParserSpecificationValidator(test,lexer_specification ).validate();
        });
        assertThrows(ParserSpecificationValidator.ValidationException.class,() -> {
            ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/left_recursion.jsonc"));
            new ParserSpecificationValidator(test,lexer_specification ).validate();
        });
        assertThrows(ParserSpecificationValidator.ValidationException.class,() -> {
            ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/missing_token.jsonc"));
            new ParserSpecificationValidator(test,lexer_specification ).validate();
        });
        assertDoesNotThrow(() -> {
            ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/valid.jsonc"));
            new ParserSpecificationValidator(test,lexer_specification ).validate();
        });
    }
}