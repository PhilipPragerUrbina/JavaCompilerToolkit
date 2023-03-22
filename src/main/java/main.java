import Lexicographer.FrontEnd.LanguageDefinition;

import java.io.File;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        LanguageDefinition langdef = new LanguageDefinition(new File("Demo/LangDef/language.jsonc"),new File("Demo/LangDef/grammar.jsonc"),new File("Demo/LangDef/tokens.jsonc"));
        System.out.println(langdef.getLanguageName());
        System.out.println(langdef.getLanguageDescription());
        System.out.println(langdef.getLanguageVersion());
        System.out.println(langdef.getTokenSpecifications());
    }
}
