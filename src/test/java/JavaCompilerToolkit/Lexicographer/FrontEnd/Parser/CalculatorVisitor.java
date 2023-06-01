package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser;

import JavaCompilerToolkit.Lexicographer.FrontEnd.AST.ASTNode;
import JavaCompilerToolkit.Lexicographer.FrontEnd.AST.ASTVisitor;
import jdk.jshell.spi.ExecutionControl;

/**
 * Simple tree walk calculator used to test parser
 */
public class CalculatorVisitor extends ASTVisitor<Double> {


    public CalculatorVisitor(ASTNode root) {
        super(root);
    }

    public double visit() throws ExecutionControl.NotImplementedException {
        return invokeVisit(getRootNode());
    }

    @Override
    protected void setup() {
        addVisitMethod(this::binary,"binary");
        addVisitMethod(this::expression,"expression");
        addVisitMethod(this::expression,"group");//do same thing
        addVisitMethod(this::literal,"literal");
        addVisitMethod(this::unary,"unary");
    }

    private double binary(ASTNode node) throws ExecutionControl.NotImplementedException {
        switch (node.getParameters().get(0).getType()) {
            case "mul":
                return invokeVisit(node.getChildren().get(0)) * invokeVisit(node.getChildren().get(1));
            case "div":
                return invokeVisit(node.getChildren().get(0)) / invokeVisit(node.getChildren().get(1));
            case "plus":
                return invokeVisit(node.getChildren().get(0)) + invokeVisit(node.getChildren().get(1));
            case "minus":
                return invokeVisit(node.getChildren().get(0)) - invokeVisit(node.getChildren().get(1));
        }
        throw new ExecutionControl.NotImplementedException("Operator not implemented");
    }
    private Double expression(ASTNode node) throws ExecutionControl.NotImplementedException {
        return invokeVisit(node.getChildren().get(0));
    }
    private Double literal(ASTNode node) throws ExecutionControl.NotImplementedException {
        if(node.getParameters().get(0).getType().equals("float")){
            return Double.parseDouble(node.getParameters().get(0).getContents());
        }
        return (double)Integer.parseInt(node.getParameters().get(0).getContents());
    }
    private Double unary(ASTNode node) throws ExecutionControl.NotImplementedException {
        return -invokeVisit(node.getChildren().get(0));
    }
}
