package ast;

import lexer.TokenType;
import lib.Variable;
import lib.Variables;

public class DeclarationStatement extends Statement {

    private boolean mutability;
    private TokenType type;
    private Expression e;

    public DeclarationStatement(boolean mutability, String varId, TokenType type, Expression e){
        if(type != TokenType.NONE && type != e.getType()){
            throw new SemanticException("Expected " + type + ", found " + e.getType());
        }
        this.mutability = mutability;
        this.type = type;
        Variables.set(varId, new Variable(this.mutability, this.type));
        this.e = new BinaryExpression(new VariableExpression(varId), TokenType.ASSIGN, e);
    }

    public DeclarationStatement(boolean mutability, String varId, TokenType type){
        this.mutability = mutability;
        this.type = type;
        this.e = null;
        Variables.set(varId, new Variable(mutability, type));
    }

    @Override
    public String getAsm86() {
        if(e == null) return "";
        else return String.format("%spop eax\n", e.getAsm86());
    }
}
