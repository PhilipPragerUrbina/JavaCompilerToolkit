package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.InterpretedLexer;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.LexerSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationWriter;
import JavaCompilerToolkit.Lexicographer.IO.OutFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ParserSpecificationTest {

    @TempDir(cleanup = CleanupMode.ON_SUCCESS) File temporary_directory;

    @Test
   void serializeDeserialize() throws Exception {
        //Deserialize
        ParserSpecification calculator_spec = new ParserSpecification(new File("src/test/resources/Calculator/grammar.jsonc"));
        //Serialize
        File out_file = new File(temporary_directory, "serialized_parser.json");
        ParserSpecificationWriter writer = new ParserSpecificationWriter(calculator_spec);
        writer.writeJSON(new OutFile(out_file));

        //Deserialize again
        ParserSpecification calculator_spec_2 = new ParserSpecification(out_file);

        //Impossible to compare, use calculator instead
        InterpretedLexer lexer = new InterpretedLexer(new LexerSpecification(new File("src/test/resources/Calculator/tokens.jsonc")));
        final String test_case = "2+5.5*2-(2+4*(-4--3))+4.3+1292.212+((2/5-(-2))) ";

        InterpretedParser a = new InterpretedParser(lexer.tokenize(test_case),calculator_spec);
        InterpretedParser b = new InterpretedParser(lexer.tokenize(test_case),calculator_spec_2);

        assertEquals(new CalculatorVisitor(a.parse()).visit(), new CalculatorVisitor(b.parse()).visit(), "Parser produces different result when serialized and deserialized");

   }

   @Test
    void validation(){
       assertThrows(IOException.class,() -> {
           ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/top_level_invalid_type.jsonc"));
       });
       assertThrows(IOException.class,() -> {
           ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/missing_entry.jsonc"));
       });
       assertThrows(IOException.class,() -> {
           ParserSpecification test = new ParserSpecification(new File("src/test/resources/ParserTestFiles/invalid_node.jsonc"));
       });
       assertDoesNotThrow(() -> {
           ParserSpecification valid = new ParserSpecification(new File("src/test/resources/ParserTestFiles/valid.jsonc"));
       });
   }
}