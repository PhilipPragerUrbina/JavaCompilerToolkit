package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.ParserSpecification;
import JavaCompilerToolkit.Lexicographer.IO.OutFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Writes parser specification to JSON
 */
public class ParserSpecificationWriter implements ParserSpecificationVisitor<Object>{

    private final ParserSpecification specification;

    /**
     * Create a writer for a specification
     * @param specification Specification to use
     */
    public ParserSpecificationWriter(ParserSpecification specification){
        this.specification = specification;
    }

    /**
     * Write the specification to a json
     * @param output Where to write to
     */
    public void writeJSON(OutFile output) throws Exception {
        JSONObject root = new JSONObject();
        root.put("start", specification.getEntry().getNodeName());
        for (TopLevelNode entry: specification.getTopLevel()) {
            root.put(entry.getNodeName(), entry.getChild().accept(this));
        }
        output.writeText(root.toString(1));
    }


    @Override
    public Object visit(OptionsNode node) throws Exception {
        JSONObject json_object = new JSONObject();
        JSONArray array = new JSONArray();
        for (ParserNode child : node.getOptions()) {
            array.put(child.accept(this));
        }
        json_object.put("options", array);
        json_object.put("save",node.getSaveName());
        json_object.put("back_track", node.getBackTrack());
        return json_object;
    }

    @Override
    public Object visit(OptionalNode node) throws Exception {
        JSONObject json_object = new JSONObject();
        json_object.put("optional", node.getChild().accept(this));
        json_object.put("save",node.getSaveName());
        json_object.put("back_track", node.getBackTrack());
        return json_object;
    }

    @Override
    public Object visit(MatchNode node) throws Exception {
        JSONObject json_object = new JSONObject();
        JSONArray array = new JSONArray();
        for (ParserNode child : node.getList()) {
            array.put(child.accept(this));
        }
        json_object.put("match", array);
        json_object.put("save",node.getSaveName());
        json_object.put("back_track", node.getBackTrack());
        return json_object;
    }

    @Override
    public Object visit(RepeatNode node) throws Exception {
        JSONObject json_object = new JSONObject();
        json_object.put("repeating", node.getChild().accept(this));
        json_object.put("min_count", node.getMinimumRepeat());

        json_object.put("back_track", node.getBackTrack());

        json_object.put("save",node.getSaveName());
        return json_object;
    }

    @Override
    public Object visit(TerminalNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(TopLevelNode node) {
        return null; //unused
     }

    @Override
    public Object visit(NonTerminalNode node) {
        return Objects.requireNonNull(node.getValue().get()).getNodeName();
    }

}
