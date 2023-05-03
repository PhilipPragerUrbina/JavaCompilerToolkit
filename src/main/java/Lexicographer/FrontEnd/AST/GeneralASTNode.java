package Lexicographer.FrontEnd.AST;

import Lexicographer.FrontEnd.Lexer.Token;

import java.util.Arrays;

/**
 * General node that can function as any kind of node
 */
public class GeneralASTNode extends ASTNode{
    private final ASTNode[] children;
    private final Token[] parameters;
    private final String type_name;

    /**
     * Create a general AST node
     * @param children children in the tree
     * @param parameters Additional information such as operands or operator type
     * @param type_name What type of node this is
     */
    public GeneralASTNode(ASTNode[] children, Token[] parameters, String type_name) {
        this.children = children;
        this.parameters = parameters;
        this.type_name = type_name;
    }

    /**
     * Get children of node in tree
     */
    public ASTNode[] getChildren() {
        return children;
    }

    /**
     * Get the name of the node type
     */
    public String getType_name() {
        return type_name;
    }

    /**
     * Get additional information about node
     */
    public Token[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "GeneralASTNode{" +
                "children=" + Arrays.toString(children) +
                ", parameters=" + Arrays.toString(parameters) +
                ", type_name='" + type_name + '\'' +
                '}';
    }
}
