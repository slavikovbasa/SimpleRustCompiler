package parser;

import ast.*;
import lexer.Token;
import lexer.TokenType;
import java.util.ArrayList;
import static lexer.TokenType.*;

/**
 * Class that contains methods to perform parsing
 */
public class Parser {

    /**
     * List of tokens of the input text
     */
    private ArrayList<Token> tokens;

    /**
     * Position of the current token in the tokens list
     */
    private static int pos;

    /**
     * End Of File token
     */
    private static final Token EOFtok = new Token("",EOF , -1, -1);

    /**
     * Variable that containes current loop hash in format @loopXXXXXXXX. Is empty if
     */
    private String loopHash;

    /**
     * Constructor takes the list of tokens
     * @param tokens list of tokens
     */
    public Parser(ArrayList<Token> tokens){
        this.tokens = new ArrayList<>(tokens);
        pos = 0;
        loopHash = "";
    }

    /**
     * Parse the input text
     * @return Block expression that contains the program
     */
    public Expression parse(){
        consume(FN);
        consume(MAIN);
        consume(LPARENT);
        consume(RPARENT);
        return block();
    }

    /**
     * Parse program block
     * @return Block expression that contains multiple expressions
     */
    private Expression block(){
        ArrayList<Expression> expressions = new ArrayList<>();
        consume(LBRACE);
        while(!match(RBRACE)){
            if(tokens.get(pos - 1) == EOFtok){
                throwException(get().getRow(), get().getCol() - 1, EOF, LBRACE);
            }
            if(get().getType() == LET) {
                Statement d = declaration();
                consume(SEMICOLON);
                expressions.add(d);
            } else if(get().getType() == IF || get().getType() == LOOP || get().getType() == WHILE ||
                    get().getType() == FOR || get().getType() == LBRACE) {
                Expression e = expression();
                if(match(SEMICOLON)){
                    expressions.add(new ExpressionStatement(e));
                } else {
                    expressions.add(e);
                }
            } else {
                Expression e = expression();
                if(get().getType() != RBRACE){
                    consume(SEMICOLON);
                    expressions.add(new ExpressionStatement(e));
                } else {
                    if(match(SEMICOLON)){
                        expressions.add(new ExpressionStatement(e));
                    } else {
                        expressions.add(e);
                    }
                }
            }
        }
        return new BlockExpression(expressions);
    }

    /**
     * Parse declaration statement
     * @return Declaration statement
     */
    private Statement declaration(){
        boolean mutable;
        Token var;
        TokenType type = TokenType.NONE;
        consume(LET);
        mutable = match(MUT);
        var = consume(VAR);
        if(match(COLON)){
            Token curr = get();
            if(match(I32)){
                type = I32;
            } else if(match(BOOL)){
                type = BOOL;
            } else {
                throwException(curr.getRow(), curr.getCol(), curr.getType(), I32, BOOL);
            }
        }
        if(match(ASSIGN)){
            Expression e = expression();
            if(type == NONE) {
                return new DeclarationStatement(mutable, var.getValue(), e.getType(), e);
            } else {
                return new DeclarationStatement(mutable, var.getValue(), type, e);
            }
        }
        return new DeclarationStatement(mutable, var.getValue(), type);
    }

    /**
     * Parse expression
     * @return Expression object
     */
    private Expression expression(){
        if(get().getType() == LBRACE)
            return block();
        else
            return assignment();
    }

    /**
     * Parse assignment expression
     * @return Assignment expression object
     */
    private Expression assignment(){
        Expression left = range();
        TokenType type = get().getType();
        if(match(ASSIGN) || match(PLUS_ASSIGN) || match(MINUS_ASSIGN) || match(ASTERISK_ASSIGN) ||
                match(SLASH_ASSIGN) || match(MOD_ASSIGN) || match(AND_ASSIGN) || match(OR_ASSIGN) ||
                match(XOR_ASSIGN) || match(LSHIFT_ASSIGN) || match(RSHIFT_ASSIGN)){
            Expression right = range();
            return new BinaryExpression(left, type, right);
        }
        return left;
    }

    /**
     * Parse range expression
     * @return Range expression object
     */
    private Expression range(){
        Expression left = lazyOR();
        if(match(RANGE)){
            Expression right = lazyOR();
            return new BinaryExpression(left, RANGE, right);
        }
        return left;
    }

    /**
     * Parse lazy or expression
     * @return Lazy or expression object
     */
    private Expression lazyOR(){
        Expression left = lazyAND(), right;
        while(match(LAZY_OR)){
            right = lazyAND();
            left = new BinaryExpression(left, LAZY_OR, right);
        }
        return left;
    }

    /**
     * Parse lazy and expression
     * @return Lazy and expression object
     */
    private Expression lazyAND(){
        Expression left = compare(), right;
        while(match(LAZY_AND)){
            right = compare();
            left = new BinaryExpression(left, LAZY_AND, right);
        }
        return left;
    }

    /**
     * Parse compare expression
     * @return Compare expression object
     */
    private Expression compare(){
        Expression left = or(), right;
        TokenType type = get().getType();
        while(match(EQ) || match(NEQ) || match(LT) || match(GT) || match(LTE) || match(GTE)){
            right = or();
            left = new BinaryExpression(left, type, right);
            type = get().getType();
        }
        return left;
    }

    /**
     * Parse or expression
     * @return Or expression object
     */
    private Expression or(){
        Expression left = xor(), right;
        while(match(OR)){
            right = xor();
            left = new BinaryExpression(left, OR, right);
        }
        return left;
    }

    /**
     * Parse xor expression
     * @return Xor expression object
     */
    private Expression xor(){
        Expression left = and(), right;
        while(match(XOR)){
            right = and();
            left = new BinaryExpression(left, XOR, right);
        }
        return left;
    }

    /**
     * Parse and expression
     * @return And expression object
     */
    private Expression and(){
        Expression left = shift(), right;
        while(match(AND)){
            right = shift();
            left = new BinaryExpression(left, AND, right);
        }
        return left;
    }

    /**
     * Parse shift expression
     * @return Shift expression object
     */
    private Expression shift(){
        Expression left = additive(), right;
        TokenType type = get().getType();
        while(match(LSHIFT) || match(RSHIFT)){
            right = additive();
            left =  new BinaryExpression(left, type, right);
            type = get().getType();
        }
        return left;
    }

    /**
     * Parse additive expression
     * @return Additive expression object
     */
    private Expression additive(){
        Expression left = multiplicative(), right;
        TokenType type = get().getType();
        while(match(PLUS) || match(MINUS)){
            right = multiplicative();
            left =  new BinaryExpression(left, type, right);
            type = get().getType();
        }
        return left;
    }

    /**
     * Parse multiplicative expression
     * @return Multiplicative expression object
     */
    private Expression multiplicative(){
        Expression left = unary(), right;
        TokenType type = get().getType();
        while(match(ASTERISK) || match(SLASH) || match(MOD)){
            right = unary();
            left =  new BinaryExpression(left, type, right);
            type = get().getType();
        }
        return left;
    }

    /**
     * Parse unary expression
     * @return Unary xpression object
     */
    private Expression unary(){
        TokenType type = get().getType();
        if(match(MINUS) || match(EXCL)){
            return new UnaryExpression(type, basic());
        }
        return basic();
    }

    /**
     * Parse basic expression
     * @return Basic expression object
     */
    private Expression basic(){
        Token curr = get();
        if(match(NUM)) {
            return new ValueExpression(Integer.parseInt(curr.getValue()));
        } else if(match(TRUE) || match(FALSE)){
            return new ValueExpression(Boolean.parseBoolean(curr.getValue()));
        } else if(match(VAR)){
            return new VariableExpression(curr.getValue());
        } else if(match(LPARENT)){
            Expression e = expression();
            consume(RPARENT);
            return e;
        } else if(get().getType() == LBRACE){
            return block();
        } else if(match(IF)){
            Expression condition = expression();
            Expression ifBlock = block();
            if(match(ELSE)){
                Expression elseBlock = block();
                return new IfExpression(condition, ifBlock, elseBlock);
            }
            return new IfExpression(condition, ifBlock);
        } else if(match(LOOP)){
            LoopExpression loop = new LoopExpression();
            loopHash = "@loop" + loop.hashCode();
            Expression e = block();
            loopHash = "";
            loop.setBlock(e);
            return loop;
        } else if(match(WHILE)){
            Expression condition = expression();
            WhileExpression loop = new WhileExpression(condition);
            loopHash = "@loop" + loop.hashCode();
            Expression block = block();
            loopHash ="";
            loop.setBlock(block);
            return loop;
        } else if(match(FOR)) {
            String var = consume(VAR).getValue();
            consume(IN);
            Expression range = range();
            ForExpression loop = new ForExpression(var, range);
            loopHash = "@loop" + loop.hashCode();
            Expression block = block();
            loopHash = "";
            loop.setBlock(block);
            return loop;
        } else if(match(BREAK)){
            return new BreakExpression(loopHash);
        } else if(match(CONTINUE)){
            return new ContinueExpression(loopHash);
        } else {
            throwException(curr.getRow(), curr.getCol(), curr.getType(), NUM, VAR, LPARENT, LBRACE, IF, LOOP, WHILE, FOR, BREAK, CONTINUE);
            return null;
        }
    }

    /**
     * Consume token - check if type matches with the current token if doesn't match - throw exception
     * @param type Expected type of token
     * @return Token that is consumed
     */
    private Token consume(TokenType type){
        Token curr = get();
        if (type == curr.getType()){
            pos++;
            return curr;
        }
        throwException(curr.getRow(), curr.getCol(), curr.getType(), type);
        return new Token("", NONE, -1, -1);
    }

    /**
     * Match type with the current token type
     * @param type Expected type
     * @return True if types match, false if they don't
     */
    private boolean match(TokenType type){
        Token curr = get();
        if (type == curr.getType()){
            pos++;
            return true;
        }
        return false;
    }

    /**
     * Get current token at pos
     * @return Current token or EOFtok if it is the end of file
     */
    private Token get() {
        if (pos >= tokens.size()) return EOFtok;
        return tokens.get(pos);
    }

    /**
     * Throws parse exception
     * @param row Row position of the unexpected token
     * @param col Column position of the unexpected token
     * @param found Type of found token
     * @param expected Types of expected tokens
     */
    private void throwException(int row, int col, TokenType found, TokenType... expected){
        StringBuilder buf = new StringBuilder("{");
        buf.append(expected[0]);
        for (int i = 1; i < expected.length; i++) {
            TokenType tokenType = expected[i];
            buf.append(" or ").append(tokenType);
        }
        buf.append("}");
        String msg = String.format("Unexpected token at %d:%d. Found {%s}, expected %s", row, col, found, buf.toString());
        throw new ParseException(msg);
    }
}
