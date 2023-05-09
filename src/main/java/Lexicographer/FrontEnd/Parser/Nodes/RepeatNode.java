package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Represents a node that matches n or more times in parse tree. (* or +)
 */
public class RepeatNode extends ParserNode{
    private final int minimum_repeat;
    private final ParserNode child;

    /**
     * Create a repeating node
     * @param minimum_repeat Minimum number to match
     * @param child Node to repeat
     */
    public RepeatNode(String save_name,boolean back_track, int minimum_repeat, ParserNode child) {
        super(save_name, back_track);
        this.minimum_repeat = minimum_repeat;
        this.child = child;
    }

    /**
     * Get the minimum number to match
     */
    public int getMinimumRepeat() {
        return minimum_repeat;
    }

    /**
     * Get the child node that should be repeated
     */
    public ParserNode getChild() {
        return child;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
