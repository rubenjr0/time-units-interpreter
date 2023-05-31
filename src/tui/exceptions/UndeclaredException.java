package tui.exceptions;

public class UndeclaredException extends Exception {
    public UndeclaredException(String identifier) {
        super(" > Identifier `" + identifier + "` has not been declared!");
    }
}
