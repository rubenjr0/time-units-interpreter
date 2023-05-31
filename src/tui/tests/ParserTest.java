package tui.tests;

import org.junit.jupiter.api.Test;
import tui.exceptions.ParserException;
import tui.parser.ExprTree;
import tui.parser.Parser;
import tui.parser.Value;
import tui.tokenizer.Token;
import tui.tokenizer.TokenType;
import tui.tokenizer.Tokenizer;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void parse_value() throws ParserException {
        Tokenizer tokens = new Tokenizer("");
        ExprTree e = Parser.parse_value(tokens, new Token(1), false);
        assertEquals(null, e.getLeft());
        assertEquals(null, e.getRight());
        Value expected = new Value(1);
        assertEquals(expected, e.getRoot());
    }

    @Test
    void parse_value_with_parens() throws ParserException {
        Tokenizer tokens = new Tokenizer(")");
        ExprTree e = Parser.parse_value(tokens, new Token(1), true);
        assertEquals(null, e.getLeft());
        assertEquals(null, e.getRight());
        Value expected = new Value(1);
        assertEquals(expected, e.getRoot());
    }

    @Test
    void parse_value_with_unit() throws ParserException {
        Tokenizer tokens = new Tokenizer("1h");
        ExprTree e = Parser.parse(tokens);
        assertEquals(null, e.getLeft());
        assertEquals(null, e.getRight());
        Value expected = new Value(1, "h");
        assertEquals(expected, e.getRoot());
    }

    @Test
    void parse_value_with_bad_unit() throws ParserException {
        Tokenizer tokens = new Tokenizer("1 seconds");
        assertThrows(ParserException.class, () ->Parser.parse(tokens));
    }

    @Test
    void parse_operation() {
        Tokenizer tokens = new Tokenizer("+ (1) (2)");
        assertDoesNotThrow(() -> Parser.parse(tokens));
    }

    @Test
    void parse_nested_operation() throws ParserException {
        Tokenizer tokens = new Tokenizer("+ (+ (1) (2)) (+ (3) (4))");
        assertDoesNotThrow(() -> Parser.parse(tokens));
    }

    @Test
    void parse_unbalanced_operation() throws ParserException {
        Tokenizer tokens = new Tokenizer("+ (* (a) (b)) (6)");
        assertDoesNotThrow(() -> Parser.parse(tokens));
    }

    @Test
    void parse_assignment() throws ParserException {
        Tokenizer tokens = new Tokenizer("a = 1");
        ExprTree expr = Parser.parse(tokens);
        ExprTree left_expr = (ExprTree) expr.getLeft();
        ExprTree right_expr = (ExprTree) expr.getRight();
        Token root = (Token) expr.getRoot();
        Token left = (Token) left_expr.getRoot();
        Value right = (Value) right_expr.getRoot();
        assertEquals(TokenType.EQUALS, root.getType());
        assertEquals(TokenType.IDENTIFIER, left.getType());
        assertEquals(new Value(1), right);
    }

    @Test
    void parse_identifier() throws ParserException {
        String id = "my_id";
        Tokenizer tokens = new Tokenizer(id);
        ExprTree expr = Parser.parse(tokens);
        Token root = (Token) expr.getRoot();
        assertEquals(null, expr.getLeft());
        assertEquals(null, expr.getLeft());
        assertEquals(TokenType.IDENTIFIER, root.getType());
        assertEquals(id, root.getStringValue());
    }

    @Test
    void malformed_operation() throws Exception {
        Tokenizer tokens = new Tokenizer("+ (1) (2");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }

    @Test
    void missing_paren() throws Exception {
        Tokenizer tokens = new Tokenizer("+ (1) (2");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }

    @Test
    void too_many_closing_parens_1() throws Exception {
        Tokenizer tokens = new Tokenizer("+ (1)) (2)");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }

    @Test
    void too_many_closing_parens_2() throws Exception {
        Tokenizer tokens = new Tokenizer("+ (1) (2))");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }

    @Test
    void too_many_opening_parens_1() throws Exception {
        Tokenizer tokens = new Tokenizer("+ ((1) (2)");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }

    @Test
    void too_many_opening_parens_2() throws Exception {
        Tokenizer tokens = new Tokenizer("+ (1) ((2)");
        assertThrows(ParserException.class, () -> Parser.parse(tokens));
    }
}