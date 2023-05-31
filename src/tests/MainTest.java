package tests;

import org.junit.jupiter.api.Test;
import tui.Interpreter;
import tui.exceptions.ParserException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void correct_program() throws Exception {
        Interpreter interpreter = new Interpreter();
        assertDoesNotThrow(() -> interpreter.from_file("good.ti"));
    }

    @Test
    void incorrect_program() throws Exception {
        Interpreter interpreter = new Interpreter();
        assertThrows(ParserException.class, () -> interpreter.from_file("bad.ti"));
    }
}