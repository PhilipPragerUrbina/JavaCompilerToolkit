package JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Defines a list of token specifications
 */
public class LexerSpecification {
    private ArrayList<TokenSpecification> specifications = null;

    /**
     * Create a lexer specification from token specifications
     * @param specifications Specifications for parsing each token
     */
    public LexerSpecification(ArrayList<TokenSpecification> specifications) {
        this.specifications = specifications;
    }

    /**
     * Create a lexer specification from a JSON file describing each token and how it is parsed
     * See examples for more information on structure.
     * @param json_file JSON or JSONC file
     * @throws JSONException Invalid JSON format
     * @throws  IOException Problems parsing or reading file
     * @throws PatternSyntaxException Invalid regex in token
     */
    public LexerSpecification(File json_file) throws IOException , JSONException, PatternSyntaxException {
        if(!json_file.exists()) throw new IOException("Unable to find lexer json file: " + json_file);
        if(!json_file.canRead()) throw new IOException("Unable to read lexer json file: " + json_file);

        JSONObject json_root = (new JSONObject(new JSONTokener(new FileReader(json_file))));
        specifications = new ArrayList<>(json_root.keySet().size());

        for (String key : json_root.keySet()){
            JSONObject contents = json_root.optJSONObject(key);
            if(contents == null) throw new IOException("Top level token JSON may only contain JSON objects: " + key);
            String pattern = contents.optString("pattern");
            if(pattern.isEmpty()) throw new IOException("Tokens must have pattern: " + key);
            int priority = contents.optInt("priority");
            if(!contents.has("priority")) throw new IOException("Tokens must have priority: " + key); //Use has since optInt defaults to 0
            boolean discard = contents.optBoolean(  "discard",false);

            //Validate pattern syntax by compiling it.
            Pattern validate_pattern = Pattern.compile(pattern);
            if(validate_pattern.matcher("").groupCount() > 1){
                throw new IOException("Token spec: " + key + " has too many capture groups. Max of one is allowed.");
            }
            specifications.add(new TokenSpecification(pattern,priority,discard,key));
        }
    }

    /**
     * Get the token specifications
     */
    public ArrayList<TokenSpecification> getSpecifications() {
        return specifications;
    }
}
