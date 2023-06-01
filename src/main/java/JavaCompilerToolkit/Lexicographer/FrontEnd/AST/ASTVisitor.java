package JavaCompilerToolkit.Lexicographer.FrontEnd.AST;

import jdk.jshell.spi.ExecutionControl;

import java.util.HashMap;

/**
 * Visits AST nodes
 * How to extend this class:
 * 1. Extend class and specify what the node visits will return
 * 2. Write your visitor methods with the signature described in VisitMethod
 * 3. Implement setup() in order to bind the visitor methods to specific node types
 * 4. Write your own visit method or methods that traverse the tree using invokeVisit() or invokeGeneralVisit(), starting at getRootNode()
 * @param <T> What visits should return
 */
public abstract class ASTVisitor<T> {
    private final ASTNode root; //Root node of AST

    /**
     * Method that visits a node
     * @param <T> Return type
     */
    @FunctionalInterface
    public interface VisitMethod<T> {
        T execute(ASTNode node) throws ExecutionControl.NotImplementedException;
    }

    private HashMap<String, VisitMethod<T>> methods = new HashMap<>();

    /**
     * Create a visitor
     * @param root Root node of AST to visit
     */
    public ASTVisitor(ASTNode root){
        setup();
        this.root = root;
    }

    /**
     * Get the root node from which visiting can be started
     */
    protected ASTNode getRootNode(){
        return root;
    }

    /**
     * Add a visit method to this visitor
     * Use this to define how your visitor works
     * @param visit_method Visitor method
     * @param type_name Name of type of node to apply to. Same as save_name in parse tree.
     * Will overwrite existing method of same name if exists.
     * Example: VisitIfStatement, if_node
     */
    protected void addVisitMethod(VisitMethod<T> visit_method, String type_name){
        methods.put(type_name,visit_method);
    }

    /**
     * Override this and add your visitor methods here
     * Visitor methods can belong to this class, use this::method_name syntax
     */
    protected abstract void setup();

    /**
     * Visit a node with a method of your choice
     * Use this instead of invokeVisit(), when the method does not care about the node type(can apply to any type)
     * @param node Node to visit
     * @param method Method to use
     * @return Return value of your choice
     */
    protected T invokeGeneralVisit(ASTNode node, VisitMethod<T> method) throws ExecutionControl.NotImplementedException {
        return method.execute(node);
    }

    /**
     * Use this to visit a node
     * @param node Node to visit
     * @return Return value of your choice
     * @throws ExecutionControl.NotImplementedException Method not defined
     */
    protected T invokeVisit(ASTNode node) throws ExecutionControl.NotImplementedException {
        if(!methods.containsKey(node.getTypeName())) throw new ExecutionControl.NotImplementedException("Node method not implemented: " + node.getTypeName());
        return methods.get(node.getTypeName()).execute(node);
    }
}
