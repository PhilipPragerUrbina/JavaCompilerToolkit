package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Takes in a lexer-specification at runtime
 */
public class InterpretedLexer implements Lexer {

    private final ArrayList<TokenSpecification> token_specifications;

    /**
     * Create a lexer at runtime a lexer specification
     * @param specification Describes tokens to use
     */
    public InterpretedLexer(LexerSpecification specification){
        this.token_specifications = specification.getSpecifications();
        token_specifications.sort(Comparator.comparing(TokenSpecification::getPriority));
    }



    public ArrayList<Token> tokenize(String file){
        ArrayList<Token> tokens = new ArrayList<>();
        for(TokenSpecification specification :  token_specifications){
            Matcher matcher = Pattern.compile(specification.getRegexPattern()).matcher(file);
            //Find, add, and remove
            file = matcher.replaceAll(match_result -> {
                if(!specification.getDiscard()) { tokens.add(new Token(specification.getName(), match_result.groupCount() == 1 ?   match_result.group(1) : null, matcher.start())); }
                return "\00".repeat(Math.max(0, matcher.end() - matcher.start())); //fill string of same size with NUL to keep position.
            });
        }
        //Sort tokens by locations
        tokens.sort(Comparator.comparingInt(Token::getLocation));
        return tokens;
    }
}
