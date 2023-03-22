package Lexicographer.FrontEnd;

/**
 * Represents how to lex a token
 */
public class TokenSpecification {
    private final String regex_pattern;
    private final int priority;
    private final boolean keep_value;
    private final String name;

    /**
     * Create a token specification that describes how to find a token
     * @param regex_pattern Pattern to use
     * @param priority Lower is higher priority
     * @param keep_value Whether or not to keep the raw value of the token(first capture group) in the AST
     * @param name Name of token
     */
    public TokenSpecification(String regex_pattern, int priority, boolean keep_value, String name) {
        this.regex_pattern = regex_pattern;
        this.priority = priority;
        this.keep_value = keep_value;
        this.name = name;
    }

    public String getRegexPattern() {
        return regex_pattern;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isKeepValue() {
        return keep_value;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "TokenSpecification{" +
                "regex_pattern='" + regex_pattern + '\'' +
                ", priority=" + priority +
                ", keep_value=" + keep_value +
                ", name='" + name + '\'' +
                '}';
    }
}
