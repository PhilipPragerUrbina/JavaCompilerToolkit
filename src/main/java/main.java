import JavaCompilerToolkit.Lexicographer.FrontEnd.AST.ASTNode;


import JavaCompilerToolkit.Lexicographer.FrontEnd.AST.ASTVisualization;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.InterpretedLexer;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.LexerSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.Token;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.InterpretedParser;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.ParserSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationWriter;
import JavaCompilerToolkit.Lexicographer.IO.OutFile;



import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//todo steal test suites from others, develop framework with json files to define expected ouput of test. Run each test with as many backends as possible. https://brson.github.io/2017/07/10/how-rust-is-tested#s-ut

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
        ASTVisualization visualization = new ASTVisualization(node);
        System.out.println(visualization.getDot());

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



    private static float calculate(ASTNode node) {
      switch (node.getTypeName()){
          case "binary":
              switch (node.getParameters().get(0).getType()) {
                  case "mul":
                      return calculate(node.getChildren().get(0)) * calculate(node.getChildren().get(1));
                  case "div":
                      return calculate(node.getChildren().get(0)) / calculate(node.getChildren().get(1));
                  case "plus":
                      return calculate(node.getChildren().get(0)) + calculate(node.getChildren().get(1));
                  case "minus":
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
