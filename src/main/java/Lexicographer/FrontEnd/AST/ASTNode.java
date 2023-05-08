package Lexicographer.FrontEnd.AST;

import Lexicographer.FrontEnd.Lexer.Token;

import java.util.ArrayList;

/**
 * General node that can function as any kind of node in an AST
 */
public class ASTNode {

    private final ArrayList<ASTNode> children;
    private final ArrayList<Token> parameters;
    private final String type_name;
    private final boolean back_track;

    /**
     * Create a general AST node
     * @param children children in the tree
     * @param parameters Additional information such as operands or operator type
     * @param type_name What type of node this is
     * @param back_track Flag for parser, tells it to combine nodes in specific cases
     */
    public ASTNode(ArrayList<ASTNode> children, ArrayList<Token> parameters, String type_name, boolean back_track) {
        this.children = children;
        this.parameters = parameters;
        this.type_name = type_name;
        this.back_track = back_track;
    }

    /**
     * Get children of node in tree
     */
    public  ArrayList<ASTNode> getChildren() {
        return children;
    }

    /**
     * Get the name of the node type
     */
    public String getTypeName() {
        return type_name;
    }

    /**
     * Get additional information about node
     */
    public ArrayList<Token> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "GeneralASTNode{" +
                "children=" + children +
                ", parameters=" + parameters +
                ", type_name='" + type_name + '\'' +
                '}';
    }

    /**
     * Flag for parser
     */
    public boolean backtrack() {
        return back_track;
    }
}
