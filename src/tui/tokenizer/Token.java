package tui.tokenizer;

import tui.parser.NodeOrLeaf;

import java.util.Objects;
import java.util.Optional;

public class Token extends NodeOrLeaf {
    TokenType type;
    Optional<String> string_value;
    Optional<Character> char_value;
    Optional<Float> float_value;

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(TokenType type, String string_value) {
        this.type = type;
        this.string_value = Optional.of(string_value);
        this.char_value = Optional.empty();
        this.float_value = Optional.empty();
    }

    public Token(float float_value) {
        this.type = TokenType.NUMBER;
        this.string_value = Optional.empty();
        this.char_value = Optional.empty();
        this.float_value = Optional.of(float_value);
    }

    public Token(char char_value) {
        this.type = TokenType.OPERATION;
        this.string_value = Optional.empty();
        this.char_value = Optional.of(char_value);
        this.float_value = Optional.empty();
    }

    public TokenType getType() {
        return type;
    }

    public String getStringValue() {
        return string_value.get();
    }

    public char getCharValue() {
        return char_value.get();
    }


    public float getFloatValue() {
        return float_value.get();
    }

    @Override
    public String toString() {
        return "Token{" + "type:" + type + ", string_value: " + string_value + ", char_value: " + char_value + ", float_value: " + float_value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(string_value, token.string_value) && Objects.equals(char_value, token.char_value) && Objects.equals(float_value, token.float_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, string_value, char_value, float_value);
    }
}
