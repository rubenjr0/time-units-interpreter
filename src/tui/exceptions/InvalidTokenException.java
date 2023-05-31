package tui.exceptions;

import tui.tokenizer.Token;

public class InvalidTokenException extends ParserException {
    public InvalidTokenException(Token token) {
        super(token.toString());
    }
}
