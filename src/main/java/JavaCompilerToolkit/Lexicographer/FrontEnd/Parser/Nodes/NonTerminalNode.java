package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

import java.lang.ref.WeakReference;

/**
 * Represents a leaf or end node that contains a recursive reference to another node
 */
public class NonTerminalNode extends ParserNode {
    private final WeakReference<TopLevelNode> value;

    /**
     * Create a non-terminal node
     * @param value Reference to top level node
     */
    public NonTerminalNode(WeakReference<TopLevelNode> value,boolean back_track, String save_name){
        super(save_name,back_track);
        this.value = value;
    }

    /**
     * Get the value of the non-terminal node
     * @return Reference to top level node in parse tree
     */
    public WeakReference<TopLevelNode> getValue(){
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) throws Exception {
        return visitor.visit(this);
    }
}
