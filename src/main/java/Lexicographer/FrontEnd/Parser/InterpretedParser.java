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
     * Info used for interpretation
     */
    public static class TraverseInfo{
        public boolean valid;
        public ASTNode node;
        public Token token;

        /**
         * Create info on a traversal
         * @param valid Did it match?
         * @param node Did it create a node?
         * @param token Did it match a token?
         */
        public TraverseInfo(boolean valid, ASTNode node, Token token) {
            this.valid = valid;
            this.node = node;
            this.token = token;
        }
    }

    @Override
    public ASTNode parse() throws Exception {
        TraverseInfo info = specification.getEntry().accept(this);
        //todo more detailed error checking, as well as warning system(Warning exception that is caught by warning manager, can have different severities and so on.)
        if(!info.valid) throw new Exception("Error parsing.");
        return info.node;
    }

    @Override
    public TraverseInfo visit(OptionsNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        boolean found = false;
        for (ParserNode option : node.getOptions()) {
            int snapshot = getSnapShot();
            TraverseInfo info = option.accept(this);
            if(!info.valid) {
                restoreSnapShot(snapshot);
                continue;
            }
            if(info.token != null){collected_tokens.add(info.token);}
            if(info.node != null){collected_nodes.add(info.node);}
            found = true;
            break;
        }
        if(!found){
            return new TraverseInfo(false, null,null); //todo exception instead
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes);
    }

    @Override
    public TraverseInfo visit(OptionalNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        int snapshot = getSnapShot();
        TraverseInfo info = node.getChild().accept(this);
        if (info.valid) {
            if (info.token != null) {
                collected_tokens.add(info.token);
            }
            if (info.node != null) {
                collected_nodes.add(info.node);
            }
        } else {
            restoreSnapShot(snapshot);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes);
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
                return new TraverseInfo(false, null,null);
            }
            if(info.token != null){collected_tokens.add(info.token);}
            if(info.node != null){collected_nodes.add(info.node);}
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes);
    }

    @Override
    public TraverseInfo visit(RepeatNode node) {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        int count = 0;
        while (true){
            int snapshot = getSnapShot();
            TraverseInfo info = node.getChild().accept(this);
            if(!info.valid) {
                restoreSnapShot(snapshot);
                break;
            }
            count++;
            if(info.token != null){collected_tokens.add(info.token);}
            if(info.node != null){collected_nodes.add(info.node);}
        }
        if(count < node.getMinimumRepeat()) { //Does not meet min requirements
            return new TraverseInfo(false, null,null);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes);
    }

    @Override
    public TraverseInfo visit(TerminalNode node) {
        boolean match = match(node.getValue());
        return new TraverseInfo(match, null, match ? last() : null);
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
    private static TraverseInfo collect(String save,ArrayList<Token> collected_tokens, ArrayList<ASTNode> collected_nodes) {
        if(save == null){
            if(collected_nodes.size() < 2){
                return new TraverseInfo(true, collected_nodes.isEmpty() ? null : collected_nodes.get(0), collected_tokens.isEmpty() ? null : collected_tokens.get(0) );
            }else{
                save= "temp";
                System.err.println("Branching node not saved, creating temporary node."); //todo change to warning
            }
        }
        return new TraverseInfo(true,new ASTNode(collected_nodes, collected_tokens,save), null);
    }

}
