package ast;

import lexer.TokenType;

public class BreakExpression implements Expression{

    private String endLoopHash;

    public BreakExpression(String loopHash){
        if(loopHash.isEmpty())
            throw new SemanticException("Using break outside the loop");
        this.endLoopHash = loopHash + "end";
    }

    @Override
    public TokenType getType() {
        return TokenType.TUPLE;
    }

    @Override
    public String getAsm86() {
        return String.format("goto %s\n", endLoopHash);
    }
}
