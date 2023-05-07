package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * Presents multiple branching options(|) in parse tree
 * One is required to match
 */
public class OptionsNode extends ParserNode{

    private final ParserNode[] options;

    /**
     * Create a node with multiple options to choose from
     * @param options Options to choose from.
     */
    public OptionsNode(ParserNode[] options, String save_name){
        super(save_name);
        this.options = options;
    }

    /**
     * Get the branches that can be taken from this node
     */
    public ParserNode[] getOptions(){
        return options;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}