package ast;

import lexer.TokenType;
import lib.Value;
import lib.Variable;
import lib.Variables;

public class ForExpression implements Expression{
    private String var;
    private Expression range;
    private Expression block;

    public ForExpression(String var, Expression range){
        if(range.getType() != TokenType.RANGE_TYPE){
            throw new SemanticException("Expected RANGE_TYPE, found " + range.getType());
        }
        this.var = var;
        this.range = range;
        Variables.set(var, new Variable(false, TokenType.I32));
        Variables.get(var).setValue(new Value(TokenType.I32));
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
        return String.format("%spop %s\npop edi\ncmp %s, edi\njnl @loop%send\n@loop%s:\n%sinc %s\ncmp %s, edi\njl @loop%s\n@loop%send:\n",
                range.getAsm86(), var, var, this.hashCode(), this.hashCode(), block.getAsm86(), var, var, this.hashCode(), this.hashCode());
    }
}
