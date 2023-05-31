package tui.parser;

import tui.exceptions.ParserException;
import tui.exceptions.PrematureEndOfInputException;
import tui.exceptions.UnexpectedTokenException;
import tui.interpreter.Value;
import tui.tokenizer.Token;
import tui.tokenizer.TokenType;
import tui.tokenizer.Tokenizer;

public class Parser {
    public static ExprTree parse(Tokenizer tokens) throws ParserException {
        ExprTree output =  Parser.parse(tokens, true);
        if (tokens.hasNext()) {
            throw new ParserException("too many tokens");
        }
        return output;
    }

    public static ExprTree parse(Tokenizer tokens, boolean first) throws ParserException {
        if (!tokens.hasNext()) {
            throw new PrematureEndOfInputException("number, operation or identifier");
        }
        Token current = tokens.next();
        return switch (current.getType()) {
            case UNIT, EQUALS, CLOSE_PAREN, OPEN_PAREN ->
                    throw new UnexpectedTokenException(current, "number, operation or identifier");
            case NUMBER -> parse_value(tokens, current, !first);
            case OPERATION -> parse_operation(tokens, current);
            case IDENTIFIER -> first ? parse_assignment(tokens, current) : parse_identifier(tokens, current);
        };
    }

    public static ExprTree parse_value(Tokenizer tokens, Token prev, boolean needs_closing_paren) throws ParserException {
        Value v;
        if (needs_closing_paren && !tokens.hasNext()) {
            throw new PrematureEndOfInputException("unit or closing parenthesis");
        }
        if (!(needs_closing_paren || tokens.hasNext())) {
            v = new Value(prev.getFloatValue());
        } else {
            Token current = tokens.next();
            if (current.getType() == TokenType.UNIT) {
                v = new Value(prev.getFloatValue(), current.getStringValue());
            } else if (needs_closing_paren && current.getType() == TokenType.CLOSE_PAREN) {
                v = new Value(prev.getFloatValue());
                tokens.go_back();
            } else {
                throw new UnexpectedTokenException(current, "unit or closing parenthesis");
            }
        }
        return new ExprTree(v, true);
    }

    private static void validate_paren(Tokenizer tokens, boolean opening) throws ParserException {
        Token expected = tokens.next();
        if (opening && expected.getType() != TokenType.OPEN_PAREN) {
            throw new UnexpectedTokenException(expected, "opening parenthesis");
        } else if (!opening && expected.getType() != TokenType.CLOSE_PAREN) {
            throw new UnexpectedTokenException(expected, "closing parenthesis");
        }
    }

    public static ExprTree parse_operation(Tokenizer tokens, Token prev) throws ParserException {
        if (!tokens.hasNext()) {
            throw new PrematureEndOfInputException("at least 2 operands");
        }
        validate_paren(tokens, true);
        ExprTree left = parse(tokens, false);
        validate_paren(tokens, false);
        validate_paren(tokens, true);
        ExprTree right = parse(tokens, false);
        validate_paren(tokens, false);
        ExprTree expr = new ExprTree(prev);
        expr.left = left;
        expr.right = right;
        return expr;
    }

    public static ExprTree parse_assignment(Tokenizer tokens, Token current) throws ParserException {
        if (tokens.hasNext()) {
            Token expected = tokens.next();
            if (expected.getType() == TokenType.EQUALS) {
                ExprTree right = parse(tokens);
                ExprTree expr = new ExprTree(expected, false);
                expr.left = new ExprTree(current);
                expr.right = right;
                return expr;
            } else {
                throw new UnexpectedTokenException(expected, "equals sign");
            }
        } else {
            return new ExprTree(current);
        }
    }

    public static ExprTree parse_identifier(Tokenizer tokens, Token current) throws ParserException {
        validate_paren(tokens, false);
        tokens.go_back();
        return new ExprTree(current);
    }
}
