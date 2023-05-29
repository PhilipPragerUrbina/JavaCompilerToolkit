package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

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
    public OptionsNode(ParserNode[] options,boolean back_track, String save_name){
        super(save_name,back_track);
        this.options = options;
    }

    /**
     * Get the branches that can be taken from this node
     */
    public ParserNode[] getOptions(){
        return options;
    }

    @Override
    public <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor) throws Exception {
        return visitor.visit(this);
    }

}
