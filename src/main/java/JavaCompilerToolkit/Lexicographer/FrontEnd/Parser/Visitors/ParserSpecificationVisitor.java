package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;

/**
 * Traverses parse tree to compile or interpret a parser
 */
public interface ParserSpecificationVisitor<ReturnType> {

    /**
     * Visit nodes and return a result if needed
     */

    ReturnType visit(OptionsNode node) throws Exception;
    ReturnType visit(OptionalNode node) throws Exception;
    ReturnType visit(MatchNode node) throws Exception;
    ReturnType visit(RepeatNode node) throws Exception;
    ReturnType visit(TerminalNode node) throws Exception;
    ReturnType visit(TopLevelNode node) throws Exception;
    ReturnType visit(NonTerminalNode node) throws Exception;

    /**
     * Unknown node is called
     */
    default ReturnType visit(ParserNode node) throws Exception{return null;}
}
