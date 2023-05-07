package Lexicographer.FrontEnd.Parser.Nodes;

import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;

/**
 * A node in the parse tree
 */
public abstract class ParserNode {

    public final String save_name;

    /**
     * Create a node in a parse tree that represents how tokens should be parser
     * @param save_name Null if node should not be saved into AST, name of AST node type if it should become a node in the AST
     *                  For example: 'binary' or 'expression' and such.
     */
    public ParserNode (String save_name){
        this.save_name = save_name;
    }

    /**
     * Get the name of the AST node that should be created from this parse node
     * @return Null if node should not be included in the AST
     */
    public String getSaveName(){
        return save_name;
    }

    /**
     * Accept a visitor for use with visitor pattern
     * Recusivley tell the visitor to process this node
     * @param visitor Visitor to use
     * @param <ReturnType> Return type if needed for visitor
     * @return Whatever was returned by visit()
     */
    public abstract <ReturnType> ReturnType accept(ParserSpecificationVisitor<ReturnType> visitor);

}
