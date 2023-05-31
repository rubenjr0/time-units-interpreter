package tui.parser;

import tui.exceptions.UndeclaredException;
import tui.tokenizer.Token;
import tui.tokenizer.TokenType;

import java.util.HashMap;

public class ExprTree implements Expr {
    NodeOrLeaf root;
    Expr left;
    Expr right;
    boolean is_value;

    public ExprTree(NodeOrLeaf root) {
        this(root, false);
    }

    public ExprTree(NodeOrLeaf root, boolean is_value) {
        this.root = root;
        this.is_value = is_value;
    }

    public NodeOrLeaf getRoot() {
        return root;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "ExprTree{" + "root=" + root + ", left=" + left + ", right=" + right + '}';
    }

    @Override
    public Value eval(HashMap<String, Value> memory) throws Exception {
        if (is_value) {
            return (Value) root;
        } else {
            Token token = (Token) root;
            if (token.getType() == TokenType.OPERATION) {
                Value left = getLeft().eval(memory);
                Value right = getRight().eval(memory);
                return switch (token.getCharValue()) {
                    case '+' -> left.add(right);
                    case '-' -> left.sub(right);
                    case '*' -> left.mul(right);
                    case '/' -> left.div(right);
                    case '^' -> left.pow(right);
                    default -> throw new UnsupportedOperationException();
                };
            } else if (token.getType() == TokenType.EQUALS) {
                String identifier = ((Token) ((ExprTree) getLeft()).root).getStringValue();
                Value v = getRight().eval(memory);
                memory.put(identifier, v);
                System.out.print(identifier + " = ");
                return v;
            } else {
                String identifier = token.getStringValue();
                Value v = memory.get(identifier);
                if (v == null) {
                    throw new UndeclaredException(identifier);
                } else {
                    return v;
                }
            }
        }
    }
}
