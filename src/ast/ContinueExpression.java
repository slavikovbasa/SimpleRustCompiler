package ast;

import lexer.TokenType;

public class ContinueExpression implements Expression{

    private String loopHash;

    public ContinueExpression(String loopHash){
        if(loopHash.isEmpty())
            throw new SemanticException("Using break outside the loop");
        this.loopHash = loopHash;
    }

    @Override
    public TokenType getType() {
        return TokenType.TUPLE;
    }

    @Override
    public String getAsm86() {
        return String.format("goto %s\n", loopHash);
    }
}
