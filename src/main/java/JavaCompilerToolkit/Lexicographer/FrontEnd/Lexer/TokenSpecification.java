package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenSpecification that = (TokenSpecification) o;
        return priority == that.priority && discard == that.discard && Objects.equals(regex_pattern, that.regex_pattern) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regex_pattern, priority, name, discard);
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
