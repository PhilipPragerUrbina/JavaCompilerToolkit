package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Represents a leaf or end node that terminates as a token
 */
public class TerminalNode extends ParserNode {
    private final String value;

    /**
     * Create a terminal node
     * @param value Name of token
     */
    public TerminalNode(String value, boolean back_track, String save_name){
        super(save_name,back_track);
        this.value = value;
    }

    /**
     * Get the value of the terminal node
     * @return Name of token
     */
    public String getValue(){
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
