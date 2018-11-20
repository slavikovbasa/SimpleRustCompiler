package ast;

import lexer.TokenType;

import static lexer.TokenType.*;

public class UnaryExpression implements Expression{
    private TokenType operation;
    private Expression right;
    private TokenType type;

    public UnaryExpression(TokenType operation, Expression right) {
        switch (operation) {
            case MINUS: {
                if (right.getType() != I32)
                    throw new SemanticException("Expected type " + I32 + ", found " + right.getType());
                type = I32;
            }
            break;
            case EXCL: {
                if (right.getType() != BOOL || right.getType() != I32)
                    throw new SemanticException("Expected type I32 or BOOL, found " + right.getType());
                type = right.getType();
            }
            break;
        }
        this.operation = operation;
        this.right = right;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getAsm86() {
        String str = "";
        switch (operation) {
            case MINUS: {
                str = right.getAsm86() + "pop eax\nnot eax\ninc eax\npush eax\n";
            }
            break;
            case EXCL: {
                if(type == I32)
                    str = right.getAsm86() + "pop eax\nnot eax\npush eax\n";
                if(type == BOOL)
                    str = right.getAsm86() + "pop eax\nxor eax, 0001h\npush eax\n";
            }
            break;
        }
        return str;
    }
}
