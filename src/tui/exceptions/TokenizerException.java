package tui.exceptions;

public class TokenizerException extends RuntimeException {
    public TokenizerException(char c) {
        super(" > Unexpected character:" + c);
    }
}
