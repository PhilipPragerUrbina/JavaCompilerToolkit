package Lexicographer.FrontEnd.Lexer;

/**
 * Represents how to lex a token
 */
public class TokenSpecification {
    private final String regex_pattern;
    private final int priority;
    private final String name;
    private final boolean discard;

    /**
     * Create a token specification that describes how to find a token
     * @param regex_pattern Pattern to use
     * @param priority Lower is higher priority
     * @param discard To not include in final token list
     * @param name Name or type of token
     */
    public TokenSpecification(String regex_pattern, int priority, boolean discard, String name) {
        this.regex_pattern = regex_pattern;
        this.priority = priority;
        this.name = name;
        this.discard = discard;
    }

    public String getRegexPattern() {
        return regex_pattern;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getDiscard(){
        return discard;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "TokenSpecification{" +
                "regex_pattern='" + regex_pattern + '\'' +
                ", priority=" + priority +
                ", name='" + name + '\'' +
                ", discard=" + discard +
                '}';
    }
}
