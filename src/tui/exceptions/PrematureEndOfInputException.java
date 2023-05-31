package tui.exceptions;

import tui.tokenizer.Token;

public class PrematureEndOfInputException extends ParserException {
    public PrematureEndOfInputException(String expected) {
        super("Premature end of input, expected " + expected);
    }
}
