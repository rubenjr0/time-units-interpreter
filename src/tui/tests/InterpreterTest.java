package tui.tests;

import org.junit.jupiter.api.Test;
import tui.interpreter.Interpreter;
import tui.exceptions.UndeclaredException;
import tui.interpreter.Value;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void noop() {
        Interpreter interpreter = new Interpreter();
        assertDoesNotThrow(() -> interpreter.eval(1, ""));
    }

    @Test
    void undeclared_identifier() {
        Interpreter interpreter = new Interpreter();
        assertThrows(UndeclaredException.class, () -> interpreter.eval(1, "a"));
    }

    @Test
    void declaration() throws Exception {
        Interpreter interpreter = new Interpreter();
        interpreter.eval(1, "a = 1");
        Value entry = interpreter.getMemory().get("a");
        Value expected = new Value(1);
        assertEquals(expected, entry);
    }

    @Test
    void just_a_number() throws Exception {
        Interpreter interpreter = new Interpreter();
        Optional<Value> output = interpreter.eval(1, "1");
        assert(output.isPresent());
        Value result = output.get();
        Value expected = new Value(1);
        assertEquals(expected, result);
    }

    @Test
    void just_a_time() throws Exception {
        Interpreter interpreter = new Interpreter();
        Optional<Value> output = interpreter.eval(1, "1h");
        assert(output.isPresent());
        Value result = output.get();
        Value expected = new Value(1, "h");
        assertEquals(expected, result);
    }

    @Test
    void good_operation() throws Exception {
        Interpreter interpreter = new Interpreter();
        // 1 + 2
        Optional<Value> output = interpreter.eval(1, "+ (1) (2)");
        assert (output.isPresent());
        Value result = output.get();
        Value expected = new Value(3);
        assertEquals(expected, result);
    }


    @Test
    void good_operation_units() throws Exception {
        Interpreter interpreter = new Interpreter();
        // 60 + 6
        Optional<Value> output = interpreter.eval(1, "+ (1 min) (6 s)");
        assert (output.isPresent());
        Value result = output.get();
        Value expected = new Value(66, "s");
        assertEquals(expected, result);
    }

    @Test
    void chained_operation() throws Exception {
        Interpreter interpreter = new Interpreter();
        // 1 + 2 + 3
        Optional<Value> output = interpreter.eval(1, "+ (1) (+ (2) (3))");
        assert (output.isPresent());
        Value result = output.get();
        Value expected = new Value(6);
        assertEquals(expected, result);
    }
}