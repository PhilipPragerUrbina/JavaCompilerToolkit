package JavaCompilerToolkit.Lexicographer.FrontEnd.AST;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.Token;

/**
 * Generates a DOT file from an AST for visualization in Graphviz
 */
public class ASTVisualization extends ASTVisitor<Void>{

    private final StringBuilder graph = new StringBuilder();

    /**
     * Create a visualization
     * @param root Root node of AST to visit
     */
    public ASTVisualization(ASTNode root) {
        super(root);
    }

    /**
     * Visit nodes and generate a graph-vis dot graph
     * @return DOT graph
     * Only run this once
     * @see "https://dreampuf.github.io/GraphvizOnline"
     */
    public String getDot(){
        graph.append("digraph G {\n");
        invokeGeneralVisit(getRootNode(), this::visitNode);
        graph.append("}\n");
        return graph.toString();
    }


    @Override
    protected void setup() {} //No setup, this class works on any node.

    private Void visitNode(ASTNode node)  {
        StringBuilder important_info = new StringBuilder();
        for (Token token : node.getParameters()) {
            important_info.append(" ").append(token.getContents() == null ? token.getType() : token.getContents());
        }
        graph.append(node.hashCode()).append(" [label=\"").append(node.getTypeName()).append(" ").append(important_info).append("\"];\n");
        for (ASTNode child : node.getChildren()) {
            graph.append(node.hashCode()).append(" -> ").append(child.hashCode()).append(";\n");
            invokeGeneralVisit(child, this::visitNode);
        }
        return null;
    }
}
