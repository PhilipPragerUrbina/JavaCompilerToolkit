package Lexicographer.FrontEnd.Lexer;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Generate Lexer code from language definition
 */
public class LexerGenerator {
    String code = "";
    public LexerGenerator(ArrayList<TokenSpecification> specifications){
        code += "Pattern[] patterns = new Pattern[]{";

        specifications.sort(Comparator.comparingInt(TokenSpecification::getPriority));

        for (TokenSpecification spec: specifications) {
            code += "Pattern.compile(\"" + spec.getRegexPattern().replaceAll("\\\\(?!\")", "\\\\\\\\") + "\"),";
        }
        code = code.substring(0, code.length()-1);
        code += "};";

        code += "\n String[] types = new String[]{";



        for (TokenSpecification spec: specifications) {
            code += "\"" + spec.getName() + "\",";
        }
        code = code.substring(0, code.length()-1);
        code += "};";
    }

    public String getResult(){
        return code;
    }
}
