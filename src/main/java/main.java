import Demo.Generated.CalculatorLexer;

import Lexicographer.FrontEnd.AST.ASTNode;

import Lexicographer.FrontEnd.LanguageDefinition;
import Lexicographer.FrontEnd.Lexer.Lexer;
import Lexicographer.FrontEnd.Lexer.LexerGenerator;
import Lexicographer.FrontEnd.Lexer.Token;
import Lexicographer.FrontEnd.Parser.InterpretedParser;
import Lexicographer.FrontEnd.Parser.ParserGenerator;
import Lexicographer.FrontEnd.Parser.ParserSpecification;
import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationWriter;
import Lexicographer.IO.InFile;
import Lexicographer.IO.OutFile;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class main {

    public static void main(String[] args) throws Exception {


        ParserSpecification specification = new ParserSpecification(new File("Calculator/LangDef/grammar.jsonc"));


        ParserSpecificationWriter writer = new ParserSpecificationWriter(specification);
        writer.writeJSON(new OutFile("out.json",true));
        ArrayList<Token> tokens = new ArrayList<>();

        InterpretedParser parser = new InterpretedParser(tokens, specification);
        ASTNode node = parser.parse();
        System.out.println(node);


        /*

        LanguageDefinition langdef = new LanguageDefinition(new File("Lox/LangDef/language.jsonc"),new File("Lox/LangDef/grammar.jsonc"),new File("Lox/LangDef/tokens.jsonc"));
        System.out.println(langdef.getLanguageName());
        System.out.println(langdef.getLanguageDescription());
        System.out.println(langdef.getLanguageVersion());
        System.out.println(langdef.getTokenSpecifications());

        LexerGenerator generator = new LexerGenerator(langdef.getTokenSpecifications(),langdef.getLanguageName(),"Demo.Generated");
        ParserGenerator parserGenerator = new ParserGenerator(langdef.getParserGrammar(), "Demo.Generated", langdef.getLanguageName());

        System.out.println("Code: \n\n");
        System.out.println(parserGenerator.getCode());
        System.out.println("\n\n");

        System.out.println("Code: \n\n");
        System.out.println(generator.getCode());
        System.out.println("\n\n");

        OutFile file = new OutFile("C:/Users/Philip/IdeaProjects/Lexicographer/src/main/java/Demo/Generated/DemoLexer.java", false);
        file.writeText(generator.getCode());

        Lexer boilerPlate = new DemoLexer();

        String in_file = new InFile("Lox/TestFiles/test.lox").readText();
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
*/



    }

   /* private static void graphVis(GeneralASTNode node) {
        String important_info = "";
        for (Token token : node.getParameters()) {
            important_info += " " + (token.getContents() == null ? token.getType() : token.getContents());
        }

        System.out.println(node.hashCode() + " [label=\"" + node.getType_name() + " " + important_info + "\"];");
        for (ASTNode child : node.getChildren()) {
            System.out.println(node.hashCode() + " -> " + ((GeneralASTNode)child).hashCode() +";" );
            graphVis((GeneralASTNode) child);
        }
    }*/
}
