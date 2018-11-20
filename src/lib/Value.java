package lib;

import lexer.TokenType;

/**
 * Class that containes bool or i32 value and its type
 */
public class Value {

    /**
     * Integer representation of the value;
     */
    private int value;

    /**
     * Type of the value;
     */
    private TokenType type;

    /**
     * Constructor for integers
     * @param value Integer value
     */
    public Value(int value){
        this.value = value;
        type = TokenType.I32;
    }

    /**
     * Constructor for booleans
     * @param value Boolean value
     */
    public Value(boolean value){
        this.value = value ? 1 : 0;
        type = TokenType.BOOL;
    }

    /**
     * Constructor for only value type
     * @param type Value type
     */
    public Value(TokenType type){
        this.type = type;
    }

    /**
     * Value getter
     * @return Int representation of the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Type getter
     * @return Value type
     */
    public TokenType getType() {
        return type;
    }
}
