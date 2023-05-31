package tui.exceptions;

import tui.tokenizer.Token;
import tui.tokenizer.TokenType;

public class UnexpectedTokenException extends ParserException {
    public UnexpectedTokenException(Token token, String expected) {
        super("Unexpected token `" + token.getType() + "`, expected " + expected);
    }
}
