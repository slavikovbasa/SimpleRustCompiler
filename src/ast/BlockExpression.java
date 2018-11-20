package ast;

import lexer.TokenType;

import java.util.ArrayList;

public class BlockExpression implements Expression{

    private ArrayList<Expression> expressions;

    public BlockExpression(ArrayList<Expression> expressions){
        this.expressions = new ArrayList<>();
        this.expressions.addAll(expressions);
    }

    @Override
    public TokenType getType() {
        return expressions.get(expressions.size() - 1).getType();
    }

    @Override
    public String getAsm86() {
        StringBuilder builder = new StringBuilder();
        for (Expression expression : expressions) {
            builder.append(expression.getAsm86());
        }
        return builder.toString();
    }
}
