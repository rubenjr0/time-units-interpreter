package tui.tokenizer;

import tui.exceptions.TokenizerException;

import java.util.ArrayList;
import java.util.Iterator;

public class Tokenizer implements Iterator<Token> {
    char[] chars;
    ArrayList<Character> buffer;
    int index;
    boolean is_identifier;
    boolean is_number;
    boolean expect_number;
    boolean is_unit;
    boolean can_be_unit;
    boolean skip_next;
    Token current;

    public Tokenizer(String input) {
        chars = input.trim().replaceAll(" ", "").toCharArray();
        buffer = new ArrayList<Character>();
    }

    @Override
    public boolean hasNext() {
        return index < chars.length;
    }

    public void go_back() {
        index--;
    }

    private Token build_identifier() {
        // build a string from the buffer
        StringBuilder builder = new StringBuilder();
        for (Character character : buffer) {
            builder.append(character);
        }
        String identifier = builder.toString();
        Token token = new Token(TokenType.IDENTIFIER, identifier);
        buffer.clear();
        is_identifier = false;
        skip_next = true;
        return token;
    }

    private Token build_unit() {
        StringBuilder builder = new StringBuilder();
        for (Character character : buffer) {
            builder.append(character);
        }
        String unit = builder.toString();
        Token token = new Token(TokenType.UNIT, unit);
        buffer.clear();
        is_unit = false;
        can_be_unit = false;
        skip_next = true;
        return token;
    }

    private Token build_number() {
        // build a float from the buffer
        StringBuilder builder = new StringBuilder();
        for (Character character : buffer) {
            builder.append(character);
        }
        String number_string = builder.toString();
        float number = Float.parseFloat(number_string);
        Token token = new Token(number);
        buffer.clear();
        is_number = false;
        expect_number = false;
        can_be_unit = true;
        skip_next = true;
        return token;
    }

    @Override
    public Token next() throws TokenizerException {
        Token token = null;
        while (token == null && hasNext()) {
            char c = chars[index];
            if (is_unit) {
                if (c != 'i' && c != 'n') {
                    token = build_unit();
                } else {
                    buffer.add(c);
                }
            } else if (is_identifier) {
                if (c != '_' && !Character.isLetterOrDigit(c)) {
                    token = build_identifier();
                } else {
                    buffer.add(c);
                }
            } else if (is_number) {
                if (c != '.' && !Character.isDigit(c)) {
                    token = build_number();
                } else {
                    buffer.add(c);
                }
            } else if (Character.isLetter(c)) {
                buffer.add(c);
                is_unit = can_be_unit;
                is_identifier = !can_be_unit;
            } else if (expect_number && !Character.isDigit(c)) {
                token = new Token(buffer.get(0));
                buffer.clear();
                expect_number = false;
                skip_next = true;
            } else if (Character.isDigit(c)) {
                buffer.add(c);
                is_number = true;
            } else if (c == '=') {
                token = new Token(TokenType.EQUALS);
            } else if (c == '-') {
                expect_number = true;
                buffer.add(c);
            } else if (c == '+' || c == '*' || c == '/' || c == '%' || c == '^') {
                token = new Token(c);
            } else if (c == '(') {
                token = new Token(TokenType.OPEN_PAREN);
            } else if (c == ')') {
                token = new Token(TokenType.CLOSE_PAREN);
                can_be_unit = false;
            } else {
                throw new TokenizerException(c);
            }
            if (skip_next) {
                skip_next = false;
            } else {
                index += 1;
            }
        }
        if (is_unit) {
            token = build_unit();
        } else if (is_identifier) {
            token = build_identifier();
        } else if (is_number) {
            token = build_number();
        }
        current = token;
        return token;
    }
}
