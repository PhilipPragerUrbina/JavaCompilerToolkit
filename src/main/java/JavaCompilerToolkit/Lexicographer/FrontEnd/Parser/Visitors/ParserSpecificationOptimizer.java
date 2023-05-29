package JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Visitors;

import JavaCompilerToolkit.Lexicographer.FrontEnd.Parser.Nodes.*;

/**
 * Rewrites left recursion and combines nodes of same type if applicable
 */
public class ParserSpecificationOptimizer implements ParserSpecificationVisitor<String>{
    //todo impl
    //todo add this: https://web.cs.ucla.edu/~todd/research/pepm08.pdf
    @Override
    public String visit(OptionsNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(OptionalNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(MatchNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(RepeatNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(TerminalNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(TopLevelNode node) throws Exception {
        return null;
    }

    @Override
    public String visit(NonTerminalNode node) throws Exception {
        return null;
    }


    // a -> a, +, a becomes ?
    // a | b | c becomes options(a,b,c)
}
