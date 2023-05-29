package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import java.util.Objects;

/**
 * A Lexer token
 * Immutable
 */
public class Token {

    private final String type;
    private final String contents;
    private final int location;

    /**
     * Create a lexer token
     * @param type Name of token type
     * @param contents Additional captured information(optional, can be null)
     * @param location Char position in file where it was found
     */
    public Token(String type, String contents, int location) {
        this.type = type;
        this.contents = contents;
        this.location = location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return location == token.location && Objects.equals(type, token.type) && Objects.equals(contents, token.contents);
    }

    /**
     * Check if token is the same type as the target type
     * @param target_type Type id
     * @return True if same
     */
    public boolean isType(String target_type){
        return type.equals(target_type);
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", contents='" + contents + '\'' +
                ", location=" + location +
                '}';
    }
}
