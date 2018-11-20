package ast;

import lexer.TokenType;
import lib.Variables;

public class VariableExpression implements Expression{
    private String name;
    private TokenType type;

    public VariableExpression(String name){
        this.name = name;
        if(!Variables.contains(name)){
            type = TokenType.NONE;
        } else {
            type = Variables.get(name).getType();
        }
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getAsm86() {
        return "push " + name + "\n";
    }

    public String toString(){
        return name;
    }
}
