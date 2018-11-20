package ast;

import lexer.TokenType;

public class WhileExpression implements Expression{
    private Expression condition;
    private Expression block;

    public WhileExpression(Expression condition){
        if(condition.getType() != TokenType.BOOL){
            throw new SemanticException("Expected BOOL, found " + condition.getType());
        }
        this.condition = condition;
    }

    public void setBlock(Expression block) {
        this.block = block;
    }

    @Override
    public TokenType getType() {
        return block.getType();
    }

    @Override
    public String getAsm86() {
        return String.format("@loop%s:\n%spop eax\ncmp eax, 0\nje @loop%send\n%sjmp @loop%s\n@loop%send:\n",
                this.hashCode(), condition.getAsm86(), this.hashCode(), block.getAsm86(), this.hashCode(), this.hashCode());
    }
}
