package ast;

public class ExpressionStatement extends Statement{
    private Expression e;

    public ExpressionStatement(Expression e){
        this.e = e;
    }

    @Override
    public String getAsm86() {
        return String.format("%spop eax\n", e.getAsm86());
    }
}
