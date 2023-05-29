package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Contains parse tree
 */
public class ParserSpecification {
    public HashMap<String, TopLevelNode> top_level;
    public String entry;

    /**
     * Create a parser specification from a valid json file describing the parse tree.
     * See examples for more information on structure.
     * @param json_file A JSON or JSONC file.
     * @throws IOException Unable to open file, or JSON in invalid for describing a parse tree.
     * @throws JSONException Unable to parse JSON itself.
     */
    public ParserSpecification(File json_file) throws IOException, JSONException {
        if(!json_file.exists()) throw new IOException("Unable to find parser json file: " + json_file);
        if(!json_file.canRead()) throw new IOException("Unable to read parser json file: " + json_file);

        JSONObject json_root = (new JSONObject(new JSONTokener(new FileReader(json_file))));

        entry = json_root.optString("start");
        if(entry == null) throw new IOException("Parser json does not define top level entry point string with key 'start'");
        if(json_root.optJSONObject(entry) == null) throw new IOException("Parser json unable to find entry point: " + entry);

        top_level = new HashMap<>(json_root.keySet().size());


        //Create top level nodes first, so references can be resolved later
        for (String key : json_root.keySet()) {
            if(key.equals("start")) continue;

            JSONObject node = json_root.optJSONObject(key);
            if(node == null) throw new IOException("Parser JSON defines top level node of invalid type: " + key);

            top_level.put(key, new TopLevelNode(null, false,null, key)); //Top level nodes can not be saved, as they don't directly contain information
        }

        for(TopLevelNode node : top_level.values()){
            node.setChild(processFromJSON(json_root.getJSONObject(node.getNodeName())));
        }

    }


    /**
     * Get entry node for visiting
     */
    public TopLevelNode getEntry(){
        return top_level.get(entry);
    }

    /**
     * Get top level nodes for visiting
     */
    public TopLevelNode[] getTopLevel(){
        TopLevelNode[] nodes = new TopLevelNode[top_level.size()];
        top_level.values().toArray(nodes);
        return nodes;
    }

    /**
     * Get top level node
     * @param name Name of top level node
     * @return null if not found
     */
    public TopLevelNode getTopLevelNode(String name){
        return top_level.get(name);
    }

    /**
     * Create an empty specification
     * @param entry Name of entry point node
     * Add nodes with addTopLevelNode()
     */
    public ParserSpecification(String entry){
        this.entry = entry;
        top_level = new HashMap<>();
    }

    /**
     * Add a top level node to the parse tree
     * @param name Name of node, as used in non-terminal leafs
     * @param node Top level node
     */
    public void addTopLevelNode(String name, TopLevelNode node){
        top_level.put(name,node);
    }


    /**
     * Convert JSON nodes to ParserNodes
     * @param parent Json parent to convert child nodes recursively
     * @return Converted node
     * @throws IOException Format is invalid
     */
    private ParserNode processFromJSON(Object parent) throws IOException {
        if (parent instanceof String) {
            if(getTopLevelNode((String)parent) != null){
                return new NonTerminalNode(new WeakReference<>(getTopLevelNode((String)parent)), false,null);
            }
            return new TerminalNode((String) parent, false,null);
        } else {
            JSONObject json_parent = (JSONObject) parent;
            JSONObject node = null;
            JSONArray list = null;

            String save_name = json_parent.optString("save");
            if(save_name.isEmpty()) save_name = null;
            boolean back_track = json_parent.optBoolean("back_track", false);

            if ((node = json_parent.optJSONObject("repeating")) != null) {
                int min_repeat = node.optInt("min_count"); //Is 0 if not specified
                return new RepeatNode(save_name,back_track, min_repeat, processFromJSON(node));
            } else if ((list = json_parent.optJSONArray("match")) != null) {
                ParserNode[] node_list = new ParserNode[list.length()];
                for (int i = 0; i < list.length(); i++) {
                    node_list[i] = processFromJSON(list.get(i));
                }
                return new MatchNode(save_name, back_track,node_list);
            } else if ((list = json_parent.optJSONArray("options")) != null) {
                ParserNode[] node_list = new ParserNode[list.length()];
                for (int i = 0; i < list.length(); i++) {
                    node_list[i] = processFromJSON(list.get(i));
                }
                return new OptionsNode(node_list, back_track,save_name);
            } else if ((node = json_parent.optJSONObject("optional")) != null) {
                return new OptionalNode(save_name, back_track,processFromJSON(node));
            }
        }
        throw new IOException("Invalid node in parser JSON: " + parent);
    }
}
