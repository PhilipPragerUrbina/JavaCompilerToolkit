package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

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
    public MatchNode(String save_name,boolean back_track, ParserNode[] list) {
        super(save_name,back_track);
        this.list = list;
    }

    /**
     * Get list of things to match
     */
    public ParserNode[] getList() {
        return list;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) throws Exception {
        return visitor.visit(this);
    }
}
