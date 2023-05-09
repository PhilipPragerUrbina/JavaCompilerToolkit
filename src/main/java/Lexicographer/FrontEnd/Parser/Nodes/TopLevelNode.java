package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Represents a top level node in the parse tree. A node with no direct parents.
 */
public class TopLevelNode extends ParserNode{
    private ParserNode child;
    private final String node_name;

    /**
     * Create a top level node
     * @param child Child node
     * @param node_name Name of node in JSON file
     */
    public TopLevelNode(String save_name,boolean back_track, ParserNode child, String node_name) {
        super(save_name,back_track);
        this.child = child;
        this.node_name = node_name;
    }

    /**
     * Set the child node
     */
    public void setChild(ParserNode child){
        this.child = child;
    }

    /**
     * Get the child node
     */
    public ParserNode getChild() {
        return child;
    }

    /**
     * Get name of node in JSON file
     */
    public String getNodeName(){
        return node_name;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
