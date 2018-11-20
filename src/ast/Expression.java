package ast;

import lexer.TokenType;

/**
 * Interface for the Expression
 */
public interface Expression {

    /**
     * Expression type getter
     * @return Expression type
     */
    TokenType getType();

    /**
     * Convert expression into asm x86 code
     * @return String that containes generated asm x86 code
     */
    String getAsm86();
}
