package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.LexerSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Lexer.TokenSpecification;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;
import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.ParserSpecification;
import java.util.Objects;

/**
 * Makes sure tokens are valid, all nodes have specified content, and detects basic left recursion.
 */
public class ParserSpecificationValidator implements ParserSpecificationVisitor<String>{
    private final ParserSpecification parser_specification;
    private final LexerSpecification token_specification;

    /**
     * Create a validator
     * @param parser_specification Parse tree to validate
     * @param token_specification Matching token spec
     */
    public ParserSpecificationValidator(ParserSpecification parser_specification, LexerSpecification token_specification){
        this.parser_specification = parser_specification;
        this.token_specification = token_specification;
    }

    public static class ValidationException extends Exception{
        public ValidationException(String message){
            super(message);
        }
    }

    /**
     * Validate the parse tree
     * @throws ValidationException Error encountered
     */
    public void validate() throws Exception {
        for (TopLevelNode entry: parser_specification.getTopLevel()) {
            String left_most_node = entry.getChild().accept(this);
            if(left_most_node.equals(entry.getNodeName())){
                throw new ValidationException("Left recursion is illegal, use optimizer to get rid of it: " + left_most_node);
            }
        }
    }


    @Override
    public String visit(OptionsNode node) throws Exception {
        if(node.getOptions().length == 0){
            throw new ValidationException("Options node is empty.");
        }
        String first = node.getOptions()[0].accept(this);
        for (int i = 1; i < node.getOptions().length; i++) {
            node.getOptions()[i].accept(this);
        }
        return first;
    }

    @Override
    public String visit(OptionalNode node) throws Exception {
        return node.getChild().accept(this);
    }

    @Override
    public String visit(MatchNode node) throws Exception {
        if(node.getList().length == 0){
            throw new ValidationException("Match node is empty.");
        }
        String first = node.getList()[0].accept(this);
        for (int i = 1; i < node.getList().length; i++) {
            node.getList()[i].accept(this);
        }
        return first;
    }

    @Override
    public String visit(RepeatNode node) throws Exception {
        return node.getChild().accept(this);
    }

    @Override
    public String visit(TerminalNode node) throws ValidationException{
        String token_name = node.getValue();
        for (TokenSpecification possible_token: token_specification.getSpecifications()) {
            if(possible_token.getName().equals(token_name)){
                return "";
            }
        }
        throw new ValidationException("Token not found: " + token_name);
    }

    @Override
    public String visit(TopLevelNode node) throws ValidationException{
        return null; //unused
    }

    @Override
    public String visit(NonTerminalNode node) throws ValidationException{
        return Objects.requireNonNull(node.getValue().get()).getNodeName();
    }
}
