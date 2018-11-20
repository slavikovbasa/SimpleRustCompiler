package ast;

import lexer.TokenType;

import static lib.Label.label;

public class IfExpression implements Expression{
    private Expression ifBlock;
    private Expression elseBlock;
    private Expression condition;
    private TokenType type;

    public IfExpression(Expression condition, Expression ifBlock){
        if(condition.getType() != TokenType.BOOL){
            throw new SemanticException("Expected BOOL, found" + condition.getType());
        }
        elseBlock = null;
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.type = ifBlock.getType();
    }

    public IfExpression(Expression condition, Expression ifBlock, Expression elseBlock){
        if(condition.getType() != TokenType.BOOL)
            throw new SemanticException("Expected BOOL, found " + condition.getType());
        this.elseBlock = elseBlock;
        this.condition = condition;
        this.ifBlock = ifBlock;
        if(ifBlock.getType() != elseBlock.getType())
            throw new SemanticException("Expected " + ifBlock.getType() + ", found " + elseBlock.getType());
        this.type = ifBlock.getType();
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getAsm86() {
        if(elseBlock == null){
            String endLabel = label();
            return String.format("%spop eax\ncmp eax, 0\nje @%s\n%s@%s:\n", condition.getAsm86(), endLabel, ifBlock.getAsm86(), endLabel);
        } else {
            String elseLabel = label();
            String endLabel = label();
            return String.format("%spop eax\ncmp eax, 0\nje @%s\n%sjmp @%s\n@%s:\n%s@%s:\n",
                    condition.getAsm86(), elseLabel, ifBlock.getAsm86(), endLabel, elseLabel, elseBlock.getAsm86(), endLabel);
        }
    }
}
