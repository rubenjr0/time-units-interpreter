package tui.tests;

import tui.exceptions.TokenizerException;
import org.junit.jupiter.api.Test;
import tui.tokenizer.Token;
import tui.tokenizer.TokenType;
import tui.tokenizer.Tokenizer;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    @Test
    void unrecognized_token() {
        Tokenizer tokenizer = new Tokenizer("&");
        assertThrows(TokenizerException.class, () -> tokenizer.next());
    }
    @Test
    void float_number() {
        Tokenizer tokenizer = new Tokenizer("1.2");
        Token token = tokenizer.next();
        Token expected = new Token(1.2f);
        assertEquals(expected, token);
    }

    @Test
    void negative_number() {
        Tokenizer tokenizer = new Tokenizer("-1");
        Token token = tokenizer.next();
        Token expected = new Token(-1);
        assertEquals(expected, token);
    }
    @Test
    void operation() {
        Tokenizer tokenizer = new Tokenizer("- (1) (2)");
        Token token = tokenizer.next();
        assertEquals(TokenType.OPERATION, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.OPEN_PAREN, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.NUMBER, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.CLOSE_PAREN, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.OPEN_PAREN, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.NUMBER, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.CLOSE_PAREN, token.getType());
    }
    @Test
    void assignment() {
        Tokenizer tokenizer = new Tokenizer("a = 1");
        Token token = tokenizer.next();
        assertEquals(TokenType.IDENTIFIER, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.EQUALS, token.getType());
        token = tokenizer.next();
        assertEquals(TokenType.NUMBER, token.getType());
    }
}