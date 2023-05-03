package Lexicographer.FrontEnd.Parser;

import Lexicographer.FrontEnd.JavaGenerator;
import Lexicographer.FrontEnd.Lexer.TokenSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Generate Java recursive decent parsing code from language definition
 */
public class ParserGenerator extends JavaGenerator {

    JSONObject root;

    /**
     * Generate parser methods for a language definition
     * @param root The grammar from the grammar JSON file.
     * @param package_name The name of the package where the parser will go.
     * @param language_name The name of the language the parser is for.
     * @throws IOException Problems using json grammar.
     */
    public ParserGenerator(JSONObject root, String package_name, String language_name) throws IOException {
        super( package_name,  "Lexicographer.FrontEnd.Parser.*"); //Import libraries
        this.root = root;

        //Generate code
        openClass(language_name + "Parser", null, "Parser");

        //constructor
        openMethod((language_name + "Parser"), "", true, "ArrayList<Token> tokens");
        addLine("super(tokens);");
        try {
            String start_method =root.getString("start");
            addLine(start_method + "();");
        }catch (JSONException exception){
            throw new IOException("'start' field not found at root of grammar config. Make sure to add a start field with the name of the top level grammar.");
        }
        closeMethod();

        for (String grammar_name: root.keySet()) {
            if(root.optJSONObject(grammar_name) == null) continue;;
            openMethod(grammar_name, "Expr",false,"");
            Condition condition = contemplateNode(root.getJSONObject(grammar_name),false);
            add(condition.action.getCode());
            closeMethod();
        }
        closeClass();
    }

    private static class Condition{
        public JavaGenerator conditional = new JavaGenerator();
        public JavaGenerator action  = new JavaGenerator();
    }

    //https://craftinginterpreters.com/parsing-expressions.html
    private Condition contemplateNode(Object uncasted_node, boolean direct_descendant){
        Condition condition = new Condition();
        if(uncasted_node instanceof String){
            String str = (String)uncasted_node;
            JSONObject recursive;
            if((recursive = root.optJSONObject(str))!=null){
                condition.action.addLine(str + "()");
            }else{
                condition.conditional.add("match(" + getStringLiteral(str) + ")");
            }
        }else{
            JSONObject node = (JSONObject)uncasted_node;
            JSONObject obj = null;
            JSONArray list = null;

            if((obj = node.optJSONObject("repeating"))!= null){
                Condition child_condition = contemplateNode(obj, true);
                condition.action.openBlock("while", child_condition.conditional.getCode());
                condition.action.addLine(child_condition.action.getCode());
                condition.action.closeBlock();
            }else if((list = node.optJSONArray("match")) != null){
                        match(condition, list, 0,direct_descendant);
            }
            else if((list = node.optJSONArray("options")) != null){
                for (Object option : list) {
                    Condition child_condition = contemplateNode(option,true);
                    if(direct_descendant || child_condition.conditional.getCode().isEmpty()){
                        condition.action.addLine(child_condition.action.getCode());
                    }else{
                        condition.action.openBlock("if", child_condition.conditional.getCode());
                        condition.action.addLine(child_condition.action.getCode());
                        condition.action.closeBlock();
                    }
                }
                condition.action.addLine("throw new Exception()");
            }else if((obj = node.optJSONObject("optional")) != null){
                Condition child_condition = contemplateNode(obj,true);
                if(direct_descendant || child_condition.conditional.getCode().isEmpty()){
                    condition.action.addLine(child_condition.action.getCode());
                }else{
                    condition.action.openBlock("if", child_condition.conditional.getCode());
                    condition.action.addLine(child_condition.action.getCode());
                    condition.action.closeBlock();
                }
            }
            String name;
            if((name = node.optString("save")) != null){
                //Pass down a list fo arguments as function parameter
                //todo needs to check the other stuff
                //todo constructor args
                //todo produce a list of node classes to create
                //condition.action.addLine( ("return new "  + name + "()"));
            }
        }
        return condition;
    }

    private void match(Condition condition, JSONArray list, int idx, boolean direct_descendant) {
        if(idx < list.length()){
            Object option = list.get(idx);
            Condition child_condition = contemplateNode(option,true);
         if(direct_descendant || child_condition.conditional.getCode().isEmpty()){
                    condition.conditional.add(child_condition.conditional.getCode());
            }else{
             condition.action.openBlock("if", child_condition.conditional.getCode());
             }
            condition.action.addLine(child_condition.action.getCode());
            match(condition,list, idx +1,true);
            if(!direct_descendant && !child_condition.conditional.getCode().isEmpty()){
                    condition.action.closeBlock();
            }

        }
    }
}