package ast;

import lexer.TokenType;
import lib.Value;

public class ValueExpression implements Expression {

    private Value value;

    public ValueExpression(int value){
        this.value = new Value(value);
    }

    public ValueExpression(boolean value){
        this.value = new Value(value);
    }

    @Override
    public TokenType getType() {
        return value.getType();
    }

    @Override
    public String getAsm86() {
        return "push " + value.getValue() + "\n";
    }
}
