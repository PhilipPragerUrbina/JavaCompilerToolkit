package Lexicographer.FrontEnd.Parser;

import Lexicographer.FrontEnd.AST.ASTNode;
import Lexicographer.FrontEnd.AST.ASTNodeGenerator;
import Lexicographer.FrontEnd.AST.GeneralASTNode;
import Lexicographer.FrontEnd.Lexer.Token;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Interprets a grammar on the fly to parse rather than generating code
 */
public class InterpretedParser extends Parser {

    JSONObject root;

    public InterpretedParser(ArrayList<Token> tokens, JSONObject grammar_root) {
        super(tokens);
        root = grammar_root;
    }

    @Override
    public ASTNode parse() throws Exception {
        String start_method =root.getString("start");
        TraverseInfo info = traverse(root.optJSONObject(start_method));
        //todo error checking
        if(!info.valid) throw new Exception("Error parsing.");
        return info.node;
    }

    private static class TraverseInfo{
        public boolean valid;
        public ASTNode node;
        public Token token;

        public TraverseInfo(boolean valid, ASTNode node, Token token) {
            this.valid = valid;
            this.node = node;
            this.token = token;
        }
    }

    //todo figure out why saving repeating is not working
    //todo figure out how to handle or rewrite self-recursive grammars

    private TraverseInfo traverse(Object parent){

        if(parent instanceof String){
            String str = (String)parent;
            JSONObject recursive;
            if((recursive = root.optJSONObject(str))!=null){
                return traverse(recursive);
            }else{ //Literal
                boolean match = match(str);
                return new TraverseInfo(match, null, match ? last() : null);
            }
        }else{
            JSONObject json_parent = (JSONObject)parent;
            JSONObject node = null;
            JSONArray list = null;

            ArrayList<Token> collected_tokens = new ArrayList<>();
            ArrayList<ASTNode> collected_nodes = new ArrayList<>();

            if((node = json_parent.optJSONObject("repeating"))!= null){
                while (true){
                    int snapshot = getSnapShot();
                 TraverseInfo info = traverse(node);
                 if(!info.valid) {
                     restoreSnapShot(snapshot);
                     break;
                 }
                 if(info.token != null){collected_tokens.add(info.token);};
                 if(info.node != null){collected_nodes.add(info.node);}
                }
            }else if((list = json_parent.optJSONArray("match")) != null){
                for (Object option : list) {
                    int snapshot = getSnapShot();
                    TraverseInfo info = traverse(option);
                    if(!info.valid) {
                        restoreSnapShot(snapshot);
                        return new TraverseInfo(false, null,null);
                    }
                    if(info.token != null){collected_tokens.add(info.token);};
                    if(info.node != null){collected_nodes.add(info.node);}
                }
            }
            else if((list = json_parent.optJSONArray("options")) != null){
                boolean found = false;
                for (Object option : list) {
                    int snapshot = getSnapShot();
                    TraverseInfo info = traverse(option);
                    if(!info.valid) {
                        restoreSnapShot(snapshot);
                        continue;
                    }
                    if(info.token != null){collected_tokens.add(info.token);};
                    if(info.node != null){collected_nodes.add(info.node);}
                    found = true;
                    break;
                }
                if(!found){
                    return new TraverseInfo(false, null,null); //todo exception instead
                }
            }else if((node = json_parent.optJSONObject("optional")) != null){
                int snapshot = getSnapShot();
                TraverseInfo info = traverse(node);
                if(info.valid) {
                    if(info.token != null){collected_tokens.add(info.token);};
                    if(info.node != null){collected_nodes.add(info.node);}
                }else{
                    restoreSnapShot(snapshot);
                }
            }

            String name = json_parent.optString("save");
            if(Objects.equals(name, "")){
                if(collected_nodes.size() < 2){
                    return new TraverseInfo(true, collected_nodes.isEmpty() ? null : collected_nodes.get(0), collected_tokens.isEmpty() ? null : collected_tokens.get(0) );
                }else{
                    //todo warning system
                    name = "temp";
                    System.err.println("Branching node not saved, creating temporary node.");
                }

            }
            ASTNode[] nodes = new ASTNode[collected_nodes.size()];
            collected_nodes.toArray(nodes);
            Token[] tokens = new Token[collected_tokens.size()];
            collected_tokens.toArray(tokens);
            return new TraverseInfo(true,new GeneralASTNode(nodes, tokens,name), null);
        }

    }



}
