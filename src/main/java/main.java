import Demo.Generated.CalculatorLexer;

import Lexicographer.FrontEnd.AST.ASTNode;


import Lexicographer.FrontEnd.Lexer.*;
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
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expression such as \"1*2+((4)+-2+3.0)*5\""); //27.0
        String expr = scanner.nextLine();

        LexerSpecification lexer_spec = new LexerSpecification(new File("Calculator/LangDef/tokens.jsonc"));
        InterpretedLexer lexer = new InterpretedLexer(lexer_spec);
        ArrayList<Token> tokens = lexer.tokenize(expr);
        System.out.println(tokens);

        ParserSpecification specification = new ParserSpecification(new File("Calculator/LangDef/grammar.jsonc"));
        ParserSpecificationWriter writer = new ParserSpecificationWriter(specification);
        writer.writeJSON(new OutFile("out.json",true));
        InterpretedParser parser = new InterpretedParser(tokens, specification);
        ASTNode node = parser.parse();
        System.out.println(node);
        System.out.println("\n\n\n");
        graphVis(node);

       System.out.println("Answer: "+ calculate(node));

        /*
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

   private static void graphVis(ASTNode node) {
        String important_info = "";
        for (Token token : node.getParameters()) {
            important_info += " " + (token.getContents() == null ? token.getType() : token.getContents());
        }

        System.out.println(node.hashCode() + " [label=\"" + node.getTypeName() + " " + important_info + "\"];");
        for (ASTNode child : node.getChildren()) {
            System.out.println(node.hashCode() + " -> " + ((ASTNode)child).hashCode() +";" );
            graphVis((ASTNode) child);
        }
    }

    private static float calculate(ASTNode node) {
      switch (node.getTypeName()){
          case "binary":
              if(node.getParameters().get(0).getType().equals("mul")){
                  return calculate(node.getChildren().get(0)) * calculate(node.getChildren().get(1));
              }else   if(node.getParameters().get(0).getType().equals("div")){
                  return calculate(node.getChildren().get(0)) / calculate(node.getChildren().get(1));
              }else   if(node.getParameters().get(0).getType().equals("plus")){
                  return calculate(node.getChildren().get(0)) + calculate(node.getChildren().get(1));
              }else   if(node.getParameters().get(0).getType().equals("minus")){
                  return calculate(node.getChildren().get(0)) - calculate(node.getChildren().get(1));
              }
          case "expression":
              return calculate(node.getChildren().get(0));
          case "group":
              return calculate(node.getChildren().get(0));
          case "literal":
              if(node.getParameters().get(0).getType().equals("float")){
                  return Float.parseFloat(node.getParameters().get(0).getContents());
              }
              return Integer.parseInt(node.getParameters().get(0).getContents());
          case "unary":
              return -calculate(node.getChildren().get(0));
      }
      return 0;

    }
}
