package Lexicographer.FrontEnd.Parser.Visitors;

import Lexicographer.FrontEnd.Parser.Nodes.*;

/**
 * Traverses parse tree to compile or interpret a parser
 */
public interface ParserSpecificationVisitor<ReturnType> {

    /**
     * Visit nodes and return a result if needed
     */

    ReturnType visit(OptionsNode node);
    ReturnType visit(OptionalNode node);
    ReturnType visit(MatchNode node);
    ReturnType visit(RepeatNode node);
    ReturnType visit(TerminalNode node);
    ReturnType visit(TopLevelNode node);
    ReturnType visit(NonTerminalNode node);

    /**
     * Unknown node is called
     */
    default ReturnType visit(ParserNode node) {return null;};
}
