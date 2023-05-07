package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Represents sequential terms in a parse tree
 * Will match children in order
 */
public class MatchNode extends ParserNode{
    private final ParserNode[] list;

    /**
     * Create a node to match terms in order
     * @param list List of things to match
     */
    public MatchNode(String save_name, ParserNode[] list) {
        super(save_name);
        this.list = list;
    }

    /**
     * Get list of things to match
     */
    public ParserNode[] getList() {
        return list;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
