package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors.ParserSpecificationVisitor;
import JavaCompilerToolkit.Lexicographer.FrontEnd.AST.ASTNode;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.Token;

import java.util.ArrayList;
import java.util.Objects;


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
        public final boolean valid;
        public final ArrayList<String> expected_tokens;
        public final ArrayList<ASTNode> nodes;
        public final ArrayList<Token> tokens;

        /**
         * Create info on a traversal
         * @param valid Did it match?
         * @param expected_tokens What did it not match?
         * @param nodes Did it create a node?
         * @param tokens Did it match a token?
         */
        public TraverseInfo(boolean valid, ArrayList<String> expected_tokens,ArrayList<ASTNode> nodes, ArrayList<Token> tokens) {
            this.valid = valid;
            this.nodes = nodes;
            this.tokens = tokens;
            this.expected_tokens = expected_tokens;
        }
    }

    @Override
    public ASTNode parse() throws Exception {
        TraverseInfo info = specification.getEntry().accept(this);
        if(!info.valid) {
            //Format
            StringBuilder message = new StringBuilder("Error parsing: Expected: ");
            for (int i = 0; i < info.expected_tokens.size()-1; i++) {
                message.append(info.expected_tokens.get(i)).append(" , ");
            }
            message.append(" or ").append(info.expected_tokens.get(info.expected_tokens.size() - 1));
            throw new ParseException(message.toString());
        }
        if(!end()) throw new ParseException("Unexpected tokens: " + peek().getType());
        return info.nodes.get(0);
    }

    @Override
    public TraverseInfo visit(OptionsNode node) throws Exception {
        //Collect information to pass up
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();

        boolean found = false;
        ArrayList<String> errors = new ArrayList<>();

        for (ParserNode option : node.getOptions()) {
            int snapshot = getSnapShot();

            TraverseInfo info = option.accept(this);

            if(!info.valid) {
                errors.addAll(info.expected_tokens);
                restoreSnapShot(snapshot); //Go back
                continue;
            }

            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
            found = true;
            break;
        }
        if(!found){
            return new TraverseInfo(false, errors,null,null);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes,node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(OptionalNode node) throws Exception {
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
    public TraverseInfo visit(MatchNode node) throws Exception {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        for (ParserNode element : node.getList()) {
            int snapshot = getSnapShot();
            TraverseInfo info = element.accept(this);
            if(!info.valid) {
                restoreSnapShot(snapshot);
                return new TraverseInfo(false, info.expected_tokens, null,null);
            }
            collected_nodes.addAll(info.nodes);
            collected_tokens.addAll(info.tokens);
        }
        return collect(node.getSaveName(), collected_tokens,collected_nodes,node.getBackTrack());
    }

    @Override
    public TraverseInfo visit(RepeatNode node) throws Exception {
        ArrayList<Token> collected_tokens = new ArrayList<>();
        ArrayList<ASTNode> collected_nodes = new ArrayList<>();
        int count = 0;
        ArrayList<String> error;
        while (true){
            int snapshot = getSnapShot();
            TraverseInfo info = node.getChild().accept(this);
            if(!info.valid) {
                error = info.expected_tokens;
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
        ArrayList<String> error = new ArrayList<>();
        if(match){
            token.add(last());
        }else{
            error.add( "'" + node.getValue() + "'");
        }
        return new TraverseInfo(match,error, new ArrayList<>(), match ? token : null);
    }

    @Override
    public TraverseInfo visit(TopLevelNode node) throws Exception {
        return node.getChild().accept(this);
    }

    @Override
    public TraverseInfo visit(NonTerminalNode node) throws Exception {
        return Objects.requireNonNull(node.getValue().get()).accept(this);
    }


    /**
     * Takes info and decides to pass up the child or create a new node
     * @param save Save string from node, can be null
     * @param collected_tokens Tokens that have been collected by node interpretation
     * @param collected_nodes AST nodes that have been collected by node interpretation
     * @param back_track Move left nodes to be the child of the node to the right(Useful in specific cases)
     * @return Info to pass up. Will either contain a new AST node, or will pass child node up
     */
    private static TraverseInfo collect(String save,ArrayList<Token> collected_tokens, ArrayList<ASTNode> collected_nodes, boolean back_track) {
        if(save == null){
            return new TraverseInfo(true, new ArrayList<>(),collected_nodes,collected_tokens);//Pass up
        }
        if(collected_nodes.size() > 1 && collected_nodes.get(1).backtrack()){ //Back track swap(Recursive)
            ASTNode node = collected_nodes.remove(0);
            collected_nodes.get(0).getChildren().add(0,node);
            return collect(save,collected_tokens,collected_nodes,back_track);
        }
        ArrayList<ASTNode> node = new ArrayList<>();
        node.add(new ASTNode(collected_nodes, collected_tokens,save, back_track));
        return new TraverseInfo(true,new ArrayList<>(),node, new ArrayList<>()); //Combine
    }

}
