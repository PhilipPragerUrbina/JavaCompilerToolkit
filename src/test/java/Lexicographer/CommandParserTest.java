package Lexicographer;

import Lexicographer.IO.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void getArguments() {
        CommandParser parser = new CommandParser(new String[]{"-flag","argument", "-flag", "argument", "--flag_2","argument"});
        //Verify multiple of same flag
        assertArrayEquals(parser.getArguments("-flag").toArray(),new String[]{"argument", "argument"}) ;
        //Verify non existent flag
        assertNull(parser.getArguments("foo"));
        //Verify simple flag
        assertArrayEquals(parser.getArguments("--flag_2").toArray(),new String[]{"argument"}) ;

        //Verify parser exception
        assertThrows(CommandParser.ArgumentSyntaxError.class, () -> {
            CommandParser parser_with_invalid_input = new CommandParser(new String[]{"argument", "-flag", "argument", "--flag_2","argument"});//No flag for first argument

        });
    }

    @Test
    void flagSpecified() {
        CommandParser parser = new CommandParser(new String[]{"-flag","argument", "-flag", "argument", "--flag_2","argument"});

        //Verify flags are found
        assertTrue(parser.flagSpecified("-flag"));
        assertTrue(parser.flagSpecified("--flag_2"));

        //Verify flags are not found
        assertFalse(parser.flagSpecified("argument"));
    }

    @Test
    void validateFlags() {
        //Validate with matching flags
        assertDoesNotThrow(() -> {
            CommandParser parser = new CommandParser(new String[]{"-flag","argument", "-flag", "argument", "--flag_2","argument"});
            parser.validateFlags("-flag","--flag_2");
        });

        //Validate with wrong flags
        assertThrows(CommandParser.ArgumentSyntaxError.class,() -> {
            CommandParser parser = new CommandParser(new String[]{"-flag","argument", "-flag", "argument", "--flag_2","argument", "-bar", "argument"});
            parser.validateFlags("-flag","--flag");
        });
    }
}