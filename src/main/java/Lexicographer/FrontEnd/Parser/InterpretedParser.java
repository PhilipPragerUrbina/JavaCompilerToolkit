package Lexicographer.FrontEnd.Parser;

import Lexicographer.FrontEnd.AST.ASTNode;

import Lexicographer.FrontEnd.Lexer.Token;
import Lexicographer.FrontEnd.Parser.Nodes.*;
import Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;
import java.util.ArrayList;


/**
 * Interprets a grammar on the fly to parse rather than generating code
 */
public class InterpretedParser extends Parser implements ParserSpecificationVisitor<InterpretedParser.TraverseInfo> {

    private final ParserSpecification specification;

    /**
     * Create a dynamic parser that interprets a parse tree
     * @param tokens Tokens to parse
     * @param specification Parse tree to interpret
     */
    public InterpretedParser(ArrayList<Token> tokens, ParserSpecification specification) {
        super(tokens);
        this.specification = specification;
    }

    /**
     * Info that is passed up
     */
    public static class TraverseInfo{
        public boolean valid;
        public String error_message;
        public ArrayList<ASTNode> nodes;
        public ArrayList<Token> tokens;

        /**
         * Create info on a traversal
         * @param valid Did it match?
         * @param nodes Did it create a node?
         * @param tokens Did it match a token?
         */
        public TraverseInfo(boolean valid, String error_message,ArrayList<ASTNode> nodes, ArrayList<Token> tokens) {
            this.valid = valid;
            this.nodes = nodes;
            this.tokens = tokens;
            this.error_message = error_message;
        }
    }

    @Override
    public ASTNode parse() throws Exception {
        TraverseInfo info = specification.getEntry().accept(this);
        //todo more detailed error checking, as well as warning system(Warning exception that is caught by warning manager, can have different severities and so on.)
        System.err.println(info.error_message);
        if(!info.valid) throw new Exception("Error parsing.");

        return info.nodes.get(0);
    }

    @Override
    public TraverseInfo visit(OptionsNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        boolean found = false;
        ArrayList<String> errors = new ArrayList<>();
        for (ParserNode option : node.getOptions()) {
            int snapshot = getSnapShot();
            TraverseInfo info = option.accept(this);
            if(!info.valid) {
                errors.add(info.error_message);
                restoreSnapShot(snapshot);
                continue;
            }
            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
            found = true;
            break;
        }
        if(!found){
            return new TraverseInfo(false, errors.toString(),null,null); //todo exception instead
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes,node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(OptionalNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        int snapshot = getSnapShot();
        TraverseInfo info = node.getChild().accept(this);
        if (info.valid) {
            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
        } else {
            restoreSnapShot(snapshot);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes,node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(MatchNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        for (ParserNode element : node.getList()) {
            int snapshot = getSnapShot();
            TraverseInfo info = element.accept(this);
            if(!info.valid) {
                restoreSnapShot(snapshot);
                return new TraverseInfo(false, info.error_message, null,null);
            }
            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes,node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(RepeatNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        int count = 0;
        String error;
        while (true){
            int snapshot = getSnapShot();
            TraverseInfo info = node.getChild().accept(this);
            if(!info.valid) {
                error = info.error_message;
                restoreSnapShot(snapshot);
                break;
            }
            count++;
            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
        }
        if(count < node.getMinimumRepeat()) { //Does not meet min requirements
            return new TraverseInfo(false,error, null,null);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes, node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(TerminalNode node) {
        boolean match = match(node.getValue());
        ArrayList<Token> token = new ArrayList<>();
        String error = "";
        if(match){
            token.add(last());
        }else{
            error = "Expected " + node.getValue();
        }
        return new TraverseInfo(match,error, new ArrayList<>(), match ? token : null);
    }

    @Override
    public TraverseInfo visit(TopLevelNode node) {
        return node.getChild().accept(this);
    }

    @Override
    public TraverseInfo visit(NonTerminalNode node) {
        return node.getValue().get().accept(this);
    }


    /**
     * Takes info and decides to pass up the child or create a new node
     * @param save Save string from node, can be null
     * @param collected_tokens Tokens that have been collected by node interpretation
     * @param collected_nodes AST nodes that have been collected by node interpretation
     * @return Info to pass up. Will either contain a new AST node, or will pass child node up
     */
    private static TraverseInfo collect(String save,ArrayList<Token> collected_tokens, ArrayList<ASTNode> collected_nodes, boolean back_track) {
        if(save == null){
            return new TraverseInfo(true, "",collected_nodes,collected_tokens);
        }
        if(collected_nodes.size() > 1 && collected_nodes.get(1).backtrack()){
            ASTNode node = collected_nodes.remove(0);
            collected_nodes.get(0).getChildren().add(0,node);
            return collect(save,collected_tokens,collected_nodes,back_track);
        }
        ArrayList<ASTNode> node = new ArrayList<>();
        node.add(new ASTNode(collected_nodes, collected_tokens,save, back_track));
        return new TraverseInfo(true,"",node, new ArrayList<>()); //Combine
    }

}
