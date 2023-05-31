package tui.parser;

import java.util.HashMap;

public interface Expr {
    public Value eval(HashMap<String, Value> memory) throws Exception;
}
