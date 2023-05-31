package tui.exceptions;

public class ParserException extends Exception {
    public ParserException(String msg) {
        super(" > Parsing error\n > " + msg);
    }
}
