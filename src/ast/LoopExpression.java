package ast;

import lexer.TokenType;

public class LoopExpression implements Expression{
    private Expression block;

    public LoopExpression(){
        block = null;
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
        return String.format("@loop%s:\n%sjmp @loop%s\n@loop%send:\n",
                this.hashCode(), block.getAsm86(), this.hashCode(), this.hashCode());
    }
}
