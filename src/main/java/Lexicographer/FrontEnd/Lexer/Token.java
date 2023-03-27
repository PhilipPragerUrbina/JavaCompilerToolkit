package Lexicographer.FrontEnd.Lexer;

/**
 * A Lexer token
 * Immutable
 */
public class Token {

    private final String type;
    private final String contents;
    private final int location;
    private final int line;

    /**
     * Create a lexer token
     * @param type Name of token type
     * @param contents Additional captured information(optional, can be null)
     * @param location Char position in file where it was found
     * @param line For debug purposes
     */
    public Token(String type, String contents, int location, int line) {
        this.type = type;
        this.contents = contents;
        this.location = location;
        this.line = line;
    }

    /**
     * Get token type or name as specified in token definition json
     */
    public String getType() {
        return type;
    }

    /**
     * Get location in string where found in chars
     */
    public int getLocation(){
        return location;
    }

    /**
     * Get optional token information that was captured, or null
     */
    public String getContents() {
        return contents;
    }

    /**
     * Get line number where token was found
     */
    public int getLine(){
        return line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", contents='" + contents + '\'' +
                ", location=" + location +
                ", line=" + line +
                '}';
    }
}
