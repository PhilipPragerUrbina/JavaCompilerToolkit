import Lexicographer.FrontEnd.Generators.JavaGenerator;
import Lexicographer.FrontEnd.LanguageDefinition;
import Lexicographer.FrontEnd.Lexer.LexerGenerator;
import Lexicographer.FrontEnd.Lexer.Token;
import Lexicographer.FrontEnd.Lexer.TokenBoilerPlate;

import java.io.File;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        LanguageDefinition langdef = new LanguageDefinition(new File("Demo/LangDef/language.jsonc"),new File("Demo/LangDef/grammar.jsonc"),new File("Demo/LangDef/tokens.jsonc"));
        System.out.println(langdef.getLanguageName());
        System.out.println(langdef.getLanguageDescription());
        System.out.println(langdef.getLanguageVersion());
        System.out.println(langdef.getTokenSpecifications());

        LexerGenerator generator = new LexerGenerator(langdef.getTokenSpecifications());
        System.out.println("Code: \n\n");
        System.out.println(generator.getResult());
        System.out.println("\n\n");
        TokenBoilerPlate boilerPlate = new TokenBoilerPlate("class Zoo {\n" +
                "  init() {\n" +
                "    this.aardvark = 1;\n" +
                "    this.baboon   = 1;\n" +
                "    this.cat      = 1;\n" +
                "    this.donkey   = 1;\n" +
                "    this.elephant = 1;\n" +
                "    this.fox      = 1;\n" +
                "  }\n" +
                "  ant()    { return this.aardvark; }\n" +
                "  banana() { return this.baboon; }\n" +
                "  tuna()   { return this.cat; }\n" +
                "  hay()    { return this.donkey; }\n" +
                "  grass()  { return this.elephant; }\n" +
                "  mouse()  { return this.fox; }\n" +
                "}\n" +
                "\n" +
                "var zoo = Zoo();\n" +
                "var sum = 0;\n" +
                "var start = clock();\n" +
                "while (sum < 100000000) {\n" +
                "  sum = sum + zoo.ant()\n" +
                "            + zoo.banana()\n" +
                "            + zoo.tuna()\n" +
                "            + zoo.hay()\n" +
                "            + zoo.grass()\n" +
                "            + zoo.mouse();\n" +
                "}\n" +
                "\n" +
                "print clock() - start;\n" +
                "print sum;");
        System.out.println(boilerPlate.getTokens());
        for (Token token: boilerPlate.getTokens()) {
            if(token.getContents() == null){
                System.out.println(token.getType());
            }else{
                System.out.println(token.getContents() + ":" + token.getType() + " ");
            }
        }


        JavaGenerator generator1 = new JavaGenerator("foo", "bar.bar");
        generator1.openClass("WHee", "par", "foo", "bar");
        generator1.createVariable("int","bar","2",true,false);
        generator1.openMethod("math", "int",true,"int g");
        generator1.addLine("print(foo);");
        generator1.closeMethod();
        generator1.closeClass();
        System.out.println(generator1.getCode());
    }
}
