package Lexicographer.FrontEnd.Lexer;

import Lexicographer.FrontEnd.JavaGenerator;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Generate Java Lexer code from language definition
 */
public class LexerGenerator extends JavaGenerator {

    /**
     * Generate Lexer Java code
     * @param specifications Tokens to generate
     * @param language_name Name of language Lexer is for
     * @param package_name Name of package that code will be part of
     */
    public LexerGenerator(ArrayList<TokenSpecification> specifications, String language_name, String package_name){
        super( package_name,  "java.util.ArrayList", "java.util.Comparator","java.util.regex.Matcher","java.util.regex.Pattern", "Lexicographer.FrontEnd.Lexer.*"); //Import libraries

        if(specifications.isEmpty()) return;

        specifications.sort(Comparator.comparingInt(TokenSpecification::getPriority)); //Sort by priority

        //Lexer uses regex to iteratively lex the whole file
        //Is not static, since regex patterns should be deallocated

        //Generate code
        openClass(language_name + "Lexer", null, "Lexer");
            comment("Sorted by priority");
            openList("private final String", "types"); //Token name information
                for (TokenSpecification spec: specifications) {
                    addListElement(spec.getDiscard() ? null : getStringLiteral(spec.getName())); //Null if discard
                }
            closeList();
            openList("private final Pattern", "patterns"); //Token regex information
                for (TokenSpecification spec: specifications) {
                    addListElement(getRegexPattern(spec.getRegexPattern()));
                }
            closeList();
            openMethod("tokenize", "ArrayList<Token>", true, "String file");
               addLine("ArrayList<Token> tokens = new ArrayList<>();");
               openBlock("for", "int i = 0; i < patterns.length; i++"); //For each token
                    addLine("Matcher matcher = patterns[i].matcher(file);");
                    addLine("String type = types[i];");
                    comment("Find, add, and remove");
                    addLine("file = matcher.replaceAll(match_result -> {"); //Find tokens
                    addLine("if(type != null) { tokens.add(new Token(type, match_result.groupCount() == 1 ?   match_result.group(1) : null, matcher.start())); }"); //Add to list if not discarded
                    addLine("return String.valueOf(\"\\00\".repeat(Math.max(0, matcher.end() - matcher.start()))); //fill string of same size with NUL to keep position."); //Replace in string with empty characters
                    addLine("});");
               closeBlock();
               comment("Sort tokens by locations");
               addLine("tokens.sort(Comparator.comparingInt(Token::getLocation));"); //Sort tokens by position
               addLine("return tokens;");
            closeMethod();
        closeClass();
    }
}
