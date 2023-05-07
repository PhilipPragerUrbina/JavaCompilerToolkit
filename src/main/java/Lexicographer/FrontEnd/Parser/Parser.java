package Lexicographer.FrontEnd.Parser;

import Lexicographer.FrontEnd.AST.ASTNode;
import Lexicographer.FrontEnd.Lexer.Token;

import java.util.ArrayList;

/**
 * Parser base class
 */
public abstract class Parser {
    private int idx = 0;
    private final ArrayList<Token> tokens;

    /**
     * Create a parser to parse a list of tokens
     * @param tokens Tokens to parse.
     */
    public Parser(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * Parse the tokens
     * @return AST root node
     */
    public abstract ASTNode parse() throws Exception;

    /**
     * Get the next token
     * @return Null if no tokens left
     */
    protected Token getNext(){
        if(end()) return null;
        idx++;
        return last();
    }

    /**
     * Check if the next token is of a type
     * @param type Type id
     * Does not move forward in token array
     */
    protected boolean check(String type){
        if(end()) return false;
        return peek().isType(type);
    }

    /**
     * Get the state(position in token array) parser is in
     */
    protected int getSnapShot(){
        return idx;
    }

    /**
     * Go back to the snapshot state
     */
    protected void restoreSnapShot(int snapshot){
        idx = snapshot;
    }

    /**
     * Check if the next token is of a type and if so, go to the next token
     * @param types Will check if the next token matches any of these type id's
     * @return True if match
     */
    protected boolean match(String... types){
        for (String type : types){
            if(check(type)){
                getNext();
                return true;
            }
        }
        return false;
    }

    /**
     * Check if at end of token list
     */
    protected boolean end(){
        return idx == tokens.size();
    }

    /**
     * Get the last token without changing state
     * Should not be called first
     */
    protected Token last(){
        return tokens.get(idx-1);
    }

    /**
     * Get the next token without changing state
     */
    protected Token peek(){
        return tokens.get(idx);
    }

}
