package Lexicographer.FrontEnd;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Reads and contains json language definition data
 */
public class LanguageDefinition {
    private JSONObject language_attributes; // Language name and stuff like that
    private JSONObject parser_grammar; // The grammar of the language
    private JSONObject lexer_tokens; // Tokens

    /**
     * Create a language definition from json or jsonc files
     * @param language_attributes_file Where the general language attributes are specified,such as name, version, etc.
     * @param parser_grammar_file Where the actual grammar is defined in a hierarchy.
     * @param lexer_tokens_file Where the tokens are defined.
     * See example LangDef for more details on how to create a language definition
     * @throws IOException Trouble reading your files
     */
    public LanguageDefinition(File language_attributes_file, File parser_grammar_file, File lexer_tokens_file) throws IOException {
        if(!language_attributes_file.exists() || !parser_grammar_file.exists() || !lexer_tokens_file.exists()) throw new IOException("A language definition file could not be opened.");

        language_attributes = (new JSONObject(new JSONTokener(new FileReader(language_attributes_file))));
        parser_grammar = (new JSONObject(new JSONTokener(new FileReader(parser_grammar_file))));
        lexer_tokens = (new JSONObject(new JSONTokener(new FileReader(lexer_tokens_file))));

        if(language_attributes == null || parser_grammar == null || lexer_tokens == null) throw new IOException("Error parsing JSON in one of the lang def files");
    }

    /**
     * Get the language name or null if not specified
     */
    public String getLanguageName(){
        try {
        return (String)language_attributes.get("name");
        }catch (JSONException exception){
            return null;
        }
    }
    /**
     * Get the language description or null if not specified
     */
    public String getLanguageDescription(){
        try {
        return (String)language_attributes.get("description");
        }catch (JSONException exception){
            return null;
        }
    }

    /**
     * Get the language version or null if not specified
     */
    public String getLanguageVersion(){
        //todo version class that supports multiple versioning schemes
        try {
            return (String)language_attributes.get("version");
        }catch (JSONException exception){
            return null;
        }
    }

    /**
     * Get a list of token specifications
     * @throws PatternSyntaxException Validation of token pattern regex failed
     * @throws IOException Missing field
     */
    public ArrayList<TokenSpecification> getTokenSpecifications() throws PatternSyntaxException,IOException {
        ArrayList<TokenSpecification> token_specification_array = new ArrayList<>(lexer_tokens.keySet().size());
        for (Object key : lexer_tokens.keySet()){
            String name = (String) key;
            JSONObject contents = (JSONObject) lexer_tokens.get(name);

            try {
                String pattern = (String) contents.get("pattern");
                Integer priority = (Integer) contents.get("priority");
                Boolean keep = (Boolean) contents.get("keep");

                //Validate pattern syntax by compiling it.
                Pattern validate_pattern = Pattern.compile(pattern);

                token_specification_array.add(new TokenSpecification(pattern,priority,keep,name));

            } catch (JSONException exception){
                throw new IOException("Token spec: " + name + " is missing a field. ");
            }
        }

        return token_specification_array;
    }


}
