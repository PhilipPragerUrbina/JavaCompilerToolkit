package Lexicographer.FrontEnd.Lexer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



//Test of what should be generated
public class TokenBoilerPlate {



    //In priority sorted
    Pattern[] patterns = new Pattern[]{Pattern.compile("\\/\\/.*\\n"),Pattern.compile("\"([^\"]+)\""),Pattern.compile("(\\d+\\.\\d+)"),Pattern.compile("(?<!\\w)while(?!\\w)"),Pattern.compile("\\=\\="),Pattern.compile("(?<!\\w)func(?!\\w)"),Pattern.compile("(?<!\\w)else(?!\\w)"),Pattern.compile("\\&\\&"),Pattern.compile("(?<!\\w)class(?!\\w)"),Pattern.compile("(?<!\\w)if(?!\\w)"),Pattern.compile("\\;"),Pattern.compile("\\|\\|"),Pattern.compile("(?<!\\w)(var|bool|int|float|string)(?!\\w)"),Pattern.compile("\\("),Pattern.compile("\\)"),Pattern.compile("(?<!\\w)(true|false)(?!\\w)"),Pattern.compile("(?<!\\w)(null)(?!\\w)"),Pattern.compile("\\<\\="),Pattern.compile("\\{"),Pattern.compile("\\>\\="),Pattern.compile("\\}"),Pattern.compile("(?<!\\w)return(?!\\w)"),Pattern.compile("\\-"),Pattern.compile("(\\d+)[^\\.\\d]{1}"),Pattern.compile("\\!"),Pattern.compile("\\>"),Pattern.compile("\\*"),Pattern.compile("\\<"),Pattern.compile("\\."),Pattern.compile("\\+"),Pattern.compile("\\="),Pattern.compile("\\/"),Pattern.compile("(\\w+)")};
    String[] types = new String[]{"comment","string","float","while","equals_equals","function_decl","else","and","class","if","end_statement","or","var","(",")","boolean","null","less_equals","{","greater_equals","}","return","minus","integer","not","greater","star","less",".","plus","equals","slash","identifier"};

    ArrayList<Token> tokens = new ArrayList<>();



    public TokenBoilerPlate(String file){

        for (int i = 0; i < patterns.length; i++) {
            Matcher matcher = patterns[i].matcher(file);
            String type = types[i];
            //Find, add, and remove
                file = matcher.replaceAll(matchResult -> {
                    tokens.add(new Token(type, matchResult.groupCount() == 1 ?   matchResult.group(1) : null, matcher.start(), 0));
                    StringBuilder s = new StringBuilder();
                    s.append("\00".repeat(Math.max(0, matcher.end() - matcher.start()))); //fill string of same size with NUL to keep position.
                    return String.valueOf(s);
                });

        }

        //Sort tokens by locations
        tokens.sort(Comparator.comparingInt(Token::getLocation));
    }

    public ArrayList<Token> getTokens(){
        return tokens;
    }

}
