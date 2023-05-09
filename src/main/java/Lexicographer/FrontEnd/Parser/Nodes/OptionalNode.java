package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Represents an optional node(?) in a parse tree
 */
public class OptionalNode extends ParserNode{
    private ParserNode child;

    /**
     * Create an optional node
     * @param child Node that should be optional
     */
    public OptionalNode(String save_name, boolean back_track,ParserNode child) {
        super(save_name,back_track);
        this.child = child;
    }

    /**
     * Get the child node that should be optional
     */
    public ParserNode getChild() {
        return child;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
