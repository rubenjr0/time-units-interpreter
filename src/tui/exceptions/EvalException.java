package tui.exceptions;

public class EvalException extends Exception {
    public EvalException(int line, Exception cause) {
        super("Error evaluating line " + line + "\n" + cause.getMessage());
    }
}
