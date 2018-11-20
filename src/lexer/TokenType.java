package lexer;

/**
 * Stores all types of tokens, such as keywords, types and operators
 */
public enum TokenType {
    //Keywords
    LET, MUT, IF, ELSE, LOOP, WHILE, FOR, IN, BREAK, CONTINUE, TRUE, FALSE,
    //Types
    I32, BOOL, NONE, RANGE_TYPE, TUPLE,
    //Operators
    COLON, SEMICOLON,
    MINUS, EXCL,
    ASTERISK, SLASH, MOD,
    PLUS, //MINUS
    RSHIFT, LSHIFT,
    AND,
    XOR,
    OR,
    EQ, NEQ, LT, GT, LTE, GTE,
    LAZY_AND,
    LAZY_OR,
    RANGE,
    ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN,
    ASTERISK_ASSIGN, SLASH_ASSIGN, MOD_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN, LSHIFT_ASSIGN, RSHIFT_ASSIGN,
    //Brackets
    LPARENT, RPARENT, LBRACE, RBRACE,
    //Extras
    VAR, NUM, FN, MAIN, EOF
}
