import Demo.Generated.DemoLexer;

import Lexicographer.FrontEnd.LanguageDefinition;
import Lexicographer.FrontEnd.Lexer.Lexer;
import Lexicographer.FrontEnd.Lexer.LexerGenerator;
import Lexicographer.FrontEnd.Lexer.Token;
import Lexicographer.IO.InFile;
import Lexicographer.IO.OutFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class main {

    public static void main(String[] args) throws IOException {

        LanguageDefinition langdef = new LanguageDefinition(new File("Demo/LangDef/language.jsonc"),new File("Demo/LangDef/grammar.jsonc"),new File("Demo/LangDef/tokens.jsonc"));
        System.out.println(langdef.getLanguageName());
        System.out.println(langdef.getLanguageDescription());
        System.out.println(langdef.getLanguageVersion());
        System.out.println(langdef.getTokenSpecifications());

        LexerGenerator generator = new LexerGenerator(langdef.getTokenSpecifications(),langdef.getLanguageName(),"Demo.Generated");
        System.out.println("Code: \n\n");
        System.out.println(generator.getCode());
        System.out.println("\n\n");

        OutFile file = new OutFile("C:/Users/Philip/IdeaProjects/Lexicographer/src/main/java/Demo/Generated/DemoLexer.java", false);
        file.writeText(generator.getCode());

        Lexer boilerPlate = new DemoLexer();

        String in_file = new InFile("Demo/TestFiles/test.lox").readText();
        ArrayList<Token> tokens = boilerPlate.tokenize(in_file);
        for (Token token: tokens) {
            if(token.getContents() == null){
                System.out.println(token.getType());
            }else{
                System.out.println(token.getContents() + ":" + token.getType() + " ");
            }
        }

        int count = 0;
        for (int i = 0; i < in_file.length(); i++) {
            if(count < tokens.size() && i == tokens.get(count).getLocation()){
                Random random = new Random(tokens.get(count).getType().hashCode());
                System.out.print("\u001B[0m");
                System.out.print("\u001B[" + (random.nextInt(7)+31) +"m");
                count++;
            }
            System.out.print(in_file.charAt(i));
        }




    }
}
