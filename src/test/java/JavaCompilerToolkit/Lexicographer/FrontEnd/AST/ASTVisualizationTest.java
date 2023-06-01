package JavaCompilerToolkit.Lexicographer.FrontEnd.AST;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.Token;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ASTVisualizationTest {
    @Test
    void getDot() throws ExecutionControl.NotImplementedException {
        //Create simple tree
        ArrayList<ASTNode> children = new ArrayList<>();
        ArrayList<Token> params = new ArrayList<>();
        params.add(new Token("foo_type", "foo_contents",0));
        params.add(new Token("bar_type", "bar_contents",0));
        children.add(new ASTNode(new ArrayList<>(), params, "bar",false));
        ASTNode root = new ASTNode(children, new ArrayList<>(), "foo", false);

        ASTVisualization visualization = new ASTVisualization(root); //Create vis

        //Compare while ignoring class ID's which are different every time
        assertEquals(("digraph G {\n" +
                "1445676990 [label=\"foo \"];\n" +
                "1445676990 -> 575628211;\n" +
                "575628211 [label=\"bar  foo_contents bar_contents\"];\n" +
                "}\n").replaceAll("\\d+", "class_id"),visualization.getDot().replaceAll("\\d+", "class_id"));

    }
}