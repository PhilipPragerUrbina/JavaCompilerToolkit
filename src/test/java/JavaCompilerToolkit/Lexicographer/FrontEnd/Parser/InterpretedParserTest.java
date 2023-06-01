package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.InterpretedLexer;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.LexerSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationWriter;
import JavaCompilerToolkit.Lexicographer.IO.OutFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InterpretedParserTest {

    //Test the interpreted parser and ast traversal using simple calculator operations with complex ordering
    @Test
    void testParsing() throws Exception {
        //Create calculator
        ParserSpecification calculator_spec = new ParserSpecification(new File("src/test/resources/Calculator/grammar.jsonc"));
        InterpretedLexer lexer = new InterpretedLexer(new LexerSpecification(new File("src/test/resources/Calculator/tokens.jsonc")));
        //Order of operations
        String test_case = "2+5.5*2-(2+4*(-4--3))+4.3+1292.212+((2/5-(-2))) ";
        double result = new CalculatorVisitor(new InterpretedParser(lexer.tokenize(test_case),calculator_spec).parse()).visit();
        assertEquals(1313.912, result);
        //Back tracking
        test_case = "3+3+3+3+3+3+3+3";
        result = new CalculatorVisitor(new InterpretedParser(lexer.tokenize(test_case),calculator_spec).parse()).visit();
        assertEquals(24.0, result);
        //Missing parenthesis(Unexpected token)
        assertThrows(ParseException.class, () -> {
            String test_case_2 = "2+5.5*2-(2+4*(-4--3)+4.3+1292.212+((2/5-(-2))) ";
            double result_2 = new CalculatorVisitor(new InterpretedParser(lexer.tokenize(test_case_2),calculator_spec).parse()).visit();
        });
        //Trialing problem(Unexpected token)
        assertThrows(ParseException.class, () -> {
            String test_case_2 = "2+5.5*2-(2+4*(-4--3)+4.3+1292.212+((2/5-(-2))) (";
            double result_2 = new CalculatorVisitor(new InterpretedParser(lexer.tokenize(test_case_2),calculator_spec).parse()).visit();
        });
        //Expected something that is not there(Expected)
        assertThrows(ParseException.class, () -> {
            String test_case_2 = " (2+2";
            double result_2 = new CalculatorVisitor(new InterpretedParser(lexer.tokenize(test_case_2),calculator_spec).parse()).visit();
        });
    }
}