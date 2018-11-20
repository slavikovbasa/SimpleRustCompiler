package ast;

import lexer.TokenType;
import lib.Value;
import lib.Variables;

import static lib.Label.label;
import static lexer.TokenType.*;

public class BinaryExpression implements Expression {
    private Expression left;
    private Expression right;
    private TokenType operation;
    private TokenType type;

    public BinaryExpression(Expression left, TokenType operation, Expression right) {
        if (left instanceof VariableExpression && operation != ASSIGN) {
            if (left.getType() == TokenType.NONE) {
                throw new SemanticException("Undefined variable " + left.toString());
            }
        }
        if (right instanceof VariableExpression) {
            if (right.getType() == TokenType.NONE) {
                throw new SemanticException("Undefined variable " + right.toString());
            }
        }
        switch (operation) {
            case ASSIGN: {
                if (!(left instanceof VariableExpression))
                    throw new SemanticException("Non-variable expressions are not allowed at the left side of the assignment");
                if (!Variables.get(left.toString()).isMutable() && Variables.get(left.toString()).getValue() != null && left.getType() != NONE)
                    throw new SemanticException("Variable " + left.toString() + " is immutable");
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != NONE && left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                Variables.get(left.toString()).setValue(new Value(right.getType()));
                type = TokenType.TUPLE;
            }
            break;
            case EQ:
            case NEQ:
            case LT:
            case GT:
            case LTE:
            case GTE: {
                if (left instanceof VariableExpression) {
                    if (Variables.get(left.toString()).getValue() == null)
                        throw new SemanticException("Variable " + left.toString() + " is not defined");
                }
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                type = BOOL;
            }
            break;
            case PLUS_ASSIGN:
            case MINUS_ASSIGN:
            case ASTERISK_ASSIGN:
            case SLASH_ASSIGN:
            case MOD_ASSIGN:
            case LSHIFT_ASSIGN:
            case RSHIFT_ASSIGN: {
                if (!(left instanceof VariableExpression))
                    throw new SemanticException("Non-variable expressions are not allowed at the left side of the assignment");
                if (!Variables.get(left.toString()).isMutable())
                    throw new SemanticException("Variable " + left.toString() + " is immutable");
                if (left.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + left.getType());
                if (right.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + right.getType());
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                Variables.get(left.toString()).setValue(new Value(right.getType()));
                type = TUPLE;
            }
            break;
            case RANGE: {
                if (left.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + left.getType());
                if (right.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + right.getType());
                if (left instanceof VariableExpression) {
                    if (Variables.get(left.toString()).getValue() == null)
                        throw new SemanticException("Variable " + left.toString() + " is not defined");
                }
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                type = RANGE_TYPE;
            }
            break;
            case LSHIFT:
            case RSHIFT:
            case PLUS:
            case MINUS:
            case ASTERISK:
            case SLASH:
            case MOD: {
                if (left.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + left.getType());
                if (right.getType() != I32)
                    throw new SemanticException("Expected " + I32 + ", found " + right.getType());
                if (left instanceof VariableExpression) {
                    if (Variables.get(left.toString()).getValue() == null)
                        throw new SemanticException("Variable " + left.toString() + " is not defined");
                }
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                type = I32;
            }
            break;
            case AND_ASSIGN:
            case OR_ASSIGN:
            case XOR_ASSIGN: {
                if (!(left instanceof VariableExpression))
                    throw new SemanticException("Non-variable expressions are not allowed at the left side of the assignment");
                if (!Variables.get(left.toString()).isMutable())
                    throw new SemanticException("Variable " + left.toString() + " is immutable");
                if (left.getType() != I32 && left.getType() != BOOL)
                    throw new SemanticException("Expected I32 or BOOL, found " + left.getType());
                if (right.getType() != I32 && right.getType() != BOOL)
                    throw new SemanticException("Expected I32 or BOOL, found " + right.getType());
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                Variables.get(left.toString()).setValue(new Value(right.getType()));
                type = TUPLE;
            }
            break;
            case AND:
            case OR:
            case XOR: {
                if (left.getType() != I32 && left.getType() != BOOL)
                    throw new SemanticException("Expected I32 or BOOL, found " + left.getType());
                if (right.getType() != I32 && right.getType() != BOOL)
                    throw new SemanticException("Expected I32 or BOOL, found " + right.getType());
                if (left.getType() != right.getType())
                    throw new SemanticException("Expected " + left.getType() + ", found " + right.getType());
                if (left instanceof VariableExpression) {
                    if (Variables.get(left.toString()).getValue() == null)
                        throw new SemanticException("Variable " + left.toString() + " is not defined");
                }
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                type = left.getType();
            }
            break;
            case LAZY_AND:
            case LAZY_OR: {
                if (left.getType() != BOOL)
                    throw new SemanticException("Expected " + BOOL + ", found " + left.getType());
                if (right.getType() != BOOL)
                    throw new SemanticException("Expected " + BOOL + ", found " + right.getType());
                if (left instanceof VariableExpression) {
                    if (Variables.get(left.toString()).getValue() == null)
                        throw new SemanticException("Variable " + left.toString() + " is not defined");
                }
                if (right instanceof VariableExpression) {
                    if (Variables.get(right.toString()).getValue() == null)
                        throw new SemanticException("Variable " + right.toString() + " is not defined");
                }
                type = BOOL;
            }
            break;
        }
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getAsm86() {
        String label1 = label();
        String label2 = label();
        switch (operation) {
            case ASSIGN:
                return String.format("%spop eax\nmov %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case EQ:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\nje @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case NEQ:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\njne @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case LT:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\njl @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case GT:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\njg @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case LTE:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\njle @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case GTE:
                return String.format("%s%spop eax\npop ebx\ncmp eax, ebx\njge @%s\npush 0\njmp @%s\n@%s: push 1\n@%s:\n",
                        left.getAsm86(), right.getAsm86(), label1, label2, label1, label2);
            case PLUS_ASSIGN:
                return String.format("%spop eax\nadd %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case MINUS_ASSIGN:
                return String.format("%spop eax\nsub %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case ASTERISK_ASSIGN:
                return String.format("%spop eax\nimul %s\nmov %s, eax\npush eax\n", right.getAsm86(), left.toString(), left.toString());
            case SLASH_ASSIGN:
                return String.format("%spop ebx\nmov eax, %s\nidiv ebx\nmov %s, eax\npush eax\n", right.getAsm86(), left.toString(), left.toString());
            case MOD_ASSIGN:
                return String.format("%spop ebx\nmov eax, %s\nidiv ebx\nmov %s, edx\npush edx\n", right.getAsm86(), left.toString(), left.toString());
            case LSHIFT_ASSIGN:
                return String.format("%spop ecx\nsal %s, ecx\npush %s\n", right.getAsm86(), left.toString(), left.toString());
            case RSHIFT_ASSIGN:
                return String.format("%spop ecx\nsar %s, ecx\npush %s\n", right.getAsm86(), left.toString(), left.toString());
            case RANGE:
                return String.format("%s%s", left.getAsm86(), right.getAsm86());
            case LSHIFT:
                return String.format("%s%spop ecx\npop eax\nsal eax, ecx\npush eax\n", left.getAsm86(), right.getAsm86());
            case RSHIFT:
                return String.format("%s%spop ecx\npop eax\nsar eax, ecx\npush eax\n", left.getAsm86(), right.getAsm86());
            case PLUS:
                return String.format("%s%spop eax\npop ebx\nadd eax, ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case MINUS:
                return String.format("%s%spop eax\npop ebx\nsub eax, ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case ASTERISK:
                return String.format("%s%spop eax\npop ebx\nimul ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case SLASH:
                return String.format("%s%spop eax\npop ebx\nidiv ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case MOD:
                return String.format("%s%spop eax\npop ebx\nidiv ebx\npush edx\n", left.getAsm86(), right.getAsm86());
            case AND_ASSIGN:
                    return String.format("%spop eax\nand %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case OR_ASSIGN:
                return String.format("%spop eax\nor %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case XOR_ASSIGN:
                return String.format("%spop eax\nxor %s, eax\npush eax\n", right.getAsm86(), left.toString());
            case AND:
                return String.format("%s%spop eax\npop ebx\nand eax, ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case OR:
                return String.format("%s%spop eax\npop ebx\nor eax, ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case XOR:
                return String.format("%s%spop eax\npop ebx\nxor eax, ebx\npush eax\n", left.getAsm86(), right.getAsm86());
            case LAZY_AND:
                return String.format("%s%spop eax\npop ebx\ncmp eax, 0\nje @%s\ncmp ebx, 0\nje @%s\npush 1\njmp @%s\n@%s: push 0\n@%s:",
                        left.getAsm86(), right.getAsm86(), label1, label1, label2, label1, label2);
            case LAZY_OR:
                return String.format("%s%spop eax\npop ebx\ncmp eax, 1\nje @%s\ncmp ebx, 1\nje @%s\npush 1\njmp @%s\n@%s: push 0\n@%s:",
                        left.getAsm86(), right.getAsm86(), label1, label1, label2, label1, label2);
        }
        return "";
    }
}
