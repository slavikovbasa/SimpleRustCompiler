package ast;

import lexer.TokenType;

/**
 * Abstract class for statements
 */
public abstract class Statement implements Expression{

    /**
     * Get statement type
     * @return TUPLE
     */
    @Override
    public TokenType getType() {
        return TokenType.TUPLE;
    }
}
