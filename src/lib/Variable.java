package lib;

import ast.SemanticException;
import lexer.TokenType;

/**
 * Class that containes information about the variable
 */
public class Variable {

    /**
     * Variable mutability
     */
    private boolean mutable;

    /**
     * Variable type
     */
    private TokenType type;

    /**
     * Variable value, mostly used to check the types
     */
    private Value value;

    /**
     * Constructor for the variable
     * @param mutable Mutability
     * @param type Type
     */
    public Variable(boolean mutable, TokenType type){
        this.mutable = mutable;
        if(type != TokenType.NONE || value == null){
            this.type = type;
        } else {
            this.type = value.getType();
        }
        this.value = null;
    }

    /**
     * Set value to the variable if it is possible
     * @param value Value that is set
     */
    public void setValue(Value value){
        if(value.getType() != type && type != TokenType.NONE)
            throw new SemanticException("Expected " + type + ", found " + value.getType());
        if(mutable || this.value == null) {
            this.value = value;
            if(type == TokenType.NONE){
                this.type = value.getType();
            }
        }
    }

    /**
     * Type getter
     * @return Type of the variable
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Value getter
     * @return Value of the variable, null if undefined
     */
    public Value getValue() {
        return value;
    }

    /**
     * Mutability getter
     * @return Mutability of the variable
     */
    public boolean isMutable() {
        return mutable;
    }
}
