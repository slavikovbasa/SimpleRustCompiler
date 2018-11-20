package lexer;

/**
 * Token that represents a program lexeme. Each token has value, type and the position in the input text
 */
public class Token {

    /**
     * String value of the token, its representation in the input text
     */
    private String value;

    /**
     * Type of the token
     */
    private TokenType type;

    /**
     * Absolute position of the token in the input text in rows and columns
     */
    private int row, col;

    /**
     * Constructor for the token
     * @param value String value of the token, its representation in the input text
     * @param type Type of the token
     * @param row Row position of the token in the input text
     * @param col Column position of the token in the input text
     */
    public Token(String value, TokenType type, int row, int col) {
        this.value = value;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    /**
     * Value getter
     * @return String value of the token, its representation in the input text
     */
    public String getValue() {
        return value;
    }

    /**
     * Type getter
     * @return Type of the token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Row getter
     * @return Row position of the token in the input text
     */
    public int getRow() {
        return row;
    }

    /**
     * Column getter
     * @return Column position of the token in the input text
     */
    public int getCol() {
        return col;
    }

    /**
     * Represent token in a string value
     * @return String representation of the token
     */
    @Override
    public String toString() {
        return String.format("%10s: %3s (%d:%d)", type, value, row, col);
    }
}

